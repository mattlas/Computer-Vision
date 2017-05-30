/**
 * Class to run the pipeline of creating a mosaic and storing the required data.
 */

#ifndef TREEMARKUPTOOLBOX_MOSAICDATA_H
#define TREEMARKUPTOOLBOX_MOSAICDATA_H

#include <string>
#include <vector>
#include "KeyPoint.h"
#include "FeaturePoints.h"
#include "imaq.h"
#include "ImageData.h"
#include "neighbours.h"
#include <memory>
//#include <thread>
#include <pthread.h>
#include <mutex>
#include <set>
#include "opencv2/core.hpp"
#include "QObject"
#include "HomographyData.h"

class MosaicData : public QObject{
    Q_OBJECT

private:
    std::vector<ImageData*> imageList;
    std::vector<std::string> directoryList;
    std::vector<std::string> pgmFileNames;
    std::string pgmFolder;
    std::vector<FeaturePoints> featurePointList;
    std::mutex readMutex;
    std::mutex writeMutex;
    std::string input_path;
    std::string pgm_path;
    ImageData* referenceImage;

    std::vector<std::vector<HomographyData*>> imagePairs;
    std::vector<HomographyData*> homographyList;

    imaq *im = NULL;

public:
    /**
     * Constructor - an empty constructor called to initiate class.
     */
    MosaicData(std::string input_path, std::string pgm_path);

public slots:
    /**
     * Add the directory to the directoryList
     * @param dir
     */
    void addDirectory(std::string dir);

    /**
     * Start the pipeline process.
     * OBS. The directoryList will need at least one directory for this function to work.
     */
    void startProcess();

    /**
     * Class wrapper to be called in threaded solution. This function then calls the threaded
     * implementation for extracting feature points.
     * @param mosaicData
     */
    static void classWrapper(MosaicData* mosaicData);



private:
    /**
     * function to read all files from input directory
     * @param string
     */
    void readFiles(std::string string);
    //std::vector<std::string> readDirectoryFiles(const std::string &dir);


    /**
     * converts all the images in folder to pgmfiles and saves files to folder of the argument.
     * @param string, output folder.
     */
    void convertToPGM(std::string string);


    /**
     * Reads in all the pgm files from a folder and saves them to pgmFilenames
     */
    void readPGMFromFolder();

    /**
     * Initiates the variable imageList and creates all the imageData objects.
     */
    void createImages();

    /**
     * Exctracts featurePoints from all the images in imageList and saves it in the ImageData
     * objects of imageList.
     */
    void extractFeaturePoints();

    /**
     * Initiates imagePairs by filling the vector with the specific image, and all of its neighbours.
     * The neighbours are used to know what images should be matched to eachother.
     * ImagePairs is a 2 dim vector filled wtih HomographyData objects.
     * The data [i][0] is the current image and [i][1 -> j-1] is all its neighbours.
     */
    void createNeighbours();

    /**
     * This function is used instead of extractFeaturePoints to extract feature points with a multithreaded
     * solution. This reduces the excecution time alot. This function calls the classWrapper which then calls
     * extractFeaturePointsThreaded();
     */
    void createThreads();

    /**
     * The function which does the featurePointsexcration with a multithreaded solution. This function is not
     * called by the user but from the function createThreads.
     */
    void extractFeaturePointsThreaded();

    /**
     * This function calculates a path starded from the referens image and spreads out throughout the
     * rest of the images. This function is starting the calculatePairs function which recursevly traverses
     * all the images with connection to the referenceImage. This is done by always selecting the path with
     * the fewest iterations to the reference image.
     */
    void findPath();

    /**
     * The recursive function from findPath. Calculates the path with the lowest number of iterations between
     * each image and the reference image.
     * @param startNode. The current node
     * @param prevNode. The previous node
     * @param recursionDepth. The number of iterations from the reference image.
     */
    void calculatePairs(std::vector<HomographyData*> startNode, HomographyData *prevNode, int recursionDepth);

    /**
     * Function to calculate all the homographies. The homographies are saved in the HomographyList.
     * HomographyList stores HomographyData which can be accessed to find each images final homography.
     * This homography is calculated to the reference image. This function calles recurseHomographies to
     * recursevly calculate the final homographies.
     * @param finalHomList
     */
    void calculateHomographies(std::vector<HomographyData*> finalHomList);

    /**
     * This is the recursive function to calculate the final homographies. The function checks the previous
     * node and multiplies its homography with its own. If the previous node have not yet got a homography
     * the function calles itself with the previous node as argument to calculate its homography first.
     * @param data. The current node.
     */
    void recurseHomographies(HomographyData* data);

    /**
     * Function to stitch the final homographies into a final mosaic. Not implemented!
     * TODO implement this function.
     */
    void stitchImages();

    /**
     * function to clear the memory. Not fully implemented.
     */
    void clearData();


signals:
    void finished();



};


#endif //TREEMARKUPTOOLBOX_MOSAICDATA_H
