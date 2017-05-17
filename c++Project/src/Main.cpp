//============================================================================
// Name        : TreeMarkupToolbox.cpp
// Author      : Machine Vision UMU
// Version     : 1.0
// Copyright   : GNU
// Description : Method for positioning field measured trees relative to image data for building a 3D model.
//============================================================================

#include <iostream>
#include "FeaturePoints.h"
#include "MosaicData.h"
#include "DirectoryReader.h"
//#include "imaq.h"


// Qt
#include "mainwindow.h"
//#include <QtGui>
#include <QApplication>
#include <QMainWindow>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    VL_PRINT ("vlfeat loaded properly\n");

    // Qt
//    QApplication app(argc, argv);
//    MainWindow w;
//    w.show();

    const char *ip_path;
    if(argc > 1){
    	ip_path = argv[1];

    } else {
        //ip_path = "/home/5dv115/Computer-Vision/c++Project/test/"; //fix this
        ip_path = "/home/5dv115/c13evk_scripts/pict"; //fix this
    }

    const char *op_path;
    if(argc > 1){
        op_path = argv[2];

    } else {
        //op_path = "/home/5dv115/Computer-Vision/c++Project/PGMdir";
        op_path = "/home/5dv115/c13evk_scripts/pict_out";
    }


    time_t start = time(0);

    // Feature points extraction
    MosaicData *data = new MosaicData();

    //data->addDirectory(ip_path);
    data->startProcess(ip_path, op_path);
    delete data;
    time_t end = time(0);

    //MosaicData *data = new MosaicData();
    //data->addDirectory(ip_path);
    //data->startProcess();
    //time_t end = time(0);
    double time = difftime(end, start) * 1000.0;
    std::cout << "time = " << time << std::endl;
    return 0;
//    return app.exec();
}
