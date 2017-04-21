//
// Created by 5dv115 on 4/21/17.
//

#include "TestClass.h"

TestClass::TestClass() {
    c1 = 0;
    str1 = "default";
}

char TestClass::get_char() {
    return c1;
}

void TestClass::set_char(char c) {
    c1 = c;
}

std::string TestClass::get_string(void) {
    return str1;
}

char *TestClass::set_string(std::string str) {
    str1 = str;
}
