#include <iostream>
#include "TestClass.h"

/*extern "C" {
#include <vl/generic.h>
}*/


int main() {
    TestClass* test = new TestClass();
    test->set_char('2');
    test->set_string("hej");

    std::cout << "Hello, World!" << std::endl;
    std::cout << test->get_char() << std::endl;
    std::cout << test->get_string() << std::endl;

    //VL_PRINT ("Hello World\n");



    return 0;
}