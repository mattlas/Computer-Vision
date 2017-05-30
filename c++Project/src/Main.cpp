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
#include <string>


// Qt
#include "mainwindow.h"
#include <QApplication>
#include <QMainWindow>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    VL_PRINT ("vlfeat loaded properly\n");
    time_t start = time(0);
    MosaicData *data = new MosaicData("/home/5dv115/c13evk_scripts/pict","/home/5dv115/c13evk_scripts/pict_out" );
    data->startProcess();
    time_t end = time(0);
    double time = difftime(end, start) * 1000.0;
    std::cout << "time = " << time << std::endl;

    // Qt
   /*QApplication app(argc, argv);

    MainWindow w;
    w.show();

    return app.exec();*/
}
