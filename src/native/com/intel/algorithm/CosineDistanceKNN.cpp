#include "com_intel_algorithm_CosineDistanceKNN.h"
#include "oneapi/dal/algo/knn.hpp"
#include "oneapi/dal/io/csv.hpp"
#include <jni.h>
#include <string>

#define JAVA_WRAPPER_CLASS "com/intel/algorithm/CosineDistanceKNN"

int createTableOnJVM(const oneapi::dal::table &table, std::string initTableMethod,
    std::string setTableMethod);

JNIEXPORT int com_intel_algorithm_CosineSimilarityKNN_calculate(
  JNIEnv *env, jclass thisClass, jint neighbors_count, jstring train_data_path, jstring query_data_path) {

    const auto train_data_file_path = (*env)->GetStringUTFChars(env, train_data_path, NULL);
    const auto query_data_file_path = (*env)->GetStringUTFChars(env, query_data_path, NULL);

    const auto x_train = dal::read<dal::table>(dal::csv::data_source{ train_data_file_path });
    const auto x_query = dal::read<dal::table>(dal::csv::data_source{ query_data_file_path });

    using cosine_desc_t = dal::cosine_distance::descriptor<float>;
    const auto cosine_desc = cosine_desc_t{};

//    const std::size_t neighbors_count = 6;
    const auto knn_desc =
        knn::descriptor<float, knn::method::brute_force, knn::task::search, cosine_desc_t>(
            neighbors_count,
            cosine_desc);

    const auto train_result = dal::train(knn_desc, x_train);
    const auto test_result = dal::infer(knn_desc, x_query, train_result.get_model());

//    std::cout << "Indices result:\n" << test_result.get_indices() << std::endl;
//    std::cout << "Distance result:\n" << test_result.get_distances() << std::endl;

    const auto indices_table =  test_result.get_indices();
    const auto distances_table = test_result.get_distances();

    int res = createTableOnJVM(indices_table, "initIndicesTable", "setIndices");
    if (res == -1) {
      return -1;
    }
    res = createTableOnJVM(distances_table, "initDistancesTable", "setDistances");
    return res;
}

// TODO: free resources.
int createTableOnJVM(const oneapi::dal::table &table, std::string initTableMethod,
    std::string setTableMethod) {
    // TODO: check whether above passed jclass can be used.
    jclass clazz = (*env)->FindClass(env, JAVA_WRAPPER_CLASS);
    if (clazz == NULL) {
      return -1;
    }
    // Call initIndicesTable
    jmethodID mid = (*env)->GetStaticMethodID(env, clazz, initTableMethod, "(I)V", "(I)V");
    (*env)->CallStaticVoidMethod(env, clazz, mid, table.get_row_count(),
      table.get_column_count());

    // Different arg type is considered.
    if (setTableMethod == "setIndices") {
      jmethodID mid = (*env)->GetStaticMethodID(env, clazz, setTableMethod, "(II)V");
    } else {
      jmethodID mid = (*env)->GetStaticMethodID(env, clazz, setTableMethod, "(IF)V");
    }

    // TODO: for indices table, data type may be converted implicitly to int.
    auto arr = oneapi::dal::row_accessor<const float>(table).pull();
    const auto x = arr.get_data();
    for (std::int64_t i = 0; i < table.get_row_count(); i++) {
        for (std::int64_t j = 0; j < table.get_column_count(); j++) {
             // TODO: check type issue
            (*env)->CallStaticVoidMethod(env, clazz, mid, i * table.get_column_count() + j,
            x[i * table.get_column_count() + j])
        }
    }
    return 0;
}