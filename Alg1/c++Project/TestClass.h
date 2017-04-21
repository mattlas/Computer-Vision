//
// Created by 5dv115 on 4/21/17.
//
#include <string>

#ifndef C_PROJECT_TESTCLASS_H
#define C_PROJECT_TESTCLASS_H


class TestClass {
    char c1;
    std::string str1;
public:
    TestClass(void);
    char get_char(void);
    void set_char(char);
    std::string get_string(void);
    char* set_string(std::string);
};


#endif //C_PROJECT_TESTCLASS_H
