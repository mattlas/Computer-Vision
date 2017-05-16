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
    QApplication app(argc, argv);
    MainWindow w;
    w.show();

    const char *path;
    if(argc > 1){
    	path = argv[1];

    } else {
        path = "/home/5dv115/c13evk_scripts/out_bigset"; //fix this
    }
    time_t start = time(0);
    MosaicData *data = new MosaicData();
    data->addDirectory(path);
    data->startProcess();
    time_t end = time(0);
    double time = difftime(end, start) * 1000.0;
    std::cout << "time = " << time << std::endl;
    return app.exec();
}
