#include <iostream>
#include "TestClass.h"

extern "C" {
#include <vl/generic.h>
}


int main() {
    TestClass* test = new TestClass();
    test->set_string("test bib");

    std::cout << test->get_string() << std::endl;

    VL_PRINT ("Hello World vlfeat\n");



    return 0;
}