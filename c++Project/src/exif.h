#ifndef __EXIF_H
#define __EXIF_H

#include <string>
/**
 * Clall used to get geotagged data from the images. Data used now is
 * Altitude, longitude and latitude
 */
namespace exif{
class EXIFInfo{
public:
	int parseFrom(const unsigned char *data, unsigned length);
	int parseFrom(const std::string &data);
	int parseFromEXIFSegment(const unsigned char *buf, unsigned len);
	void clear();
	char ByteAlign;                  	// 0 = Motorola byte alignment, 1 = Intel
	std::string ImageDescription;     	// Image description
	std::string Make;                	// Camera manufacturer's name
	std::string Model;               	// Camera model
	unsigned short Orientation;		// Image orientation, start of data corresponds to
						// 0: unspecified in EXIF data
						// 1: upper left of image
						// 3: lower right of image
						// 6: upper right of image
						// 8: lower left of image
						// 9: undefined
	unsigned short BitsPerSample;     	// Number of bits per component
	std::string Software;           	// Software used
	std::string DateTime;           	// File change date and time
	std::string DateTimeOriginal; 		// Original file date and time (may not exist)
	std::string DateTimeDigitized; 		// Digitization date and time (may not exist)
	std::string SubSecTimeOriginal; 	// Sub-second time that original picture was taken
	std::string Copyright;            	// File copyright information
	double ExposureTime;              	// Exposure time in seconds
	double FNumber;                   	// F/stop
	unsigned short ExposureProgram;		// Exposure program
						// 0: Not defined
						// 1: Manual
						// 2: Normal program
						// 3: Aperture priority
						// 4: Shutter priority
						// 5: Creative program
						// 6: Action program
						// 7: Portrait mode
						// 8: Landscape mode
	unsigned short ISOSpeedRatings;		// ISO speed
	double ShutterSpeedValue;		// Shutter speed (reciprocal of exposure time)
	double ExposureBiasValue;		// Exposure bias value in EV
	double SubjectDistance;			// Distance to focus point in meters
	double FocalLength;			// Focal length of lens in millimeters
	unsigned short FocalLengthIn35mm;	// Focal length in 35mm film
	char Flash;         			// 0 = no flash, 1 = flash used
	unsigned short FlashReturnedLight;	// Flash returned light status
						// 0: No strobe return detection function
						// 1: Reserved
						// 2: Strobe return light not detected
						// 3: Strobe return light detected
	unsigned short FlashMode;		// Flash mode
						// 0: Unknown
						// 1: Compulsory flash firing
						// 2: Compulsory flash suppression
						// 3: Automatic mode
	unsigned short MeteringMode;		// Metering mode
						// 1: average
						// 2: center weighted average
						// 3: spot
						// 4: multi-spot
						// 5: multi-segment
	unsigned ImageWidth;			// Image width reported in EXIF data
	unsigned ImageHeight;			// Image height reported in EXIF data
	struct Geolocation_t{			// GPS information embedded in file
		double Latitude;		// Image latitude expressed as decimal
		double Longitude;		// Image longitude expressed as decimal
		double Altitude;      		// Altitude in meters, relative to sea level
		char AltitudeRef;		// 0 = above sea level, -1 = below sea level
		double DOP;			// GPS degree of precision (DOP)
		struct Coord_t{
			double degrees;
			double minutes;
			double seconds;
			char direction;
		} LatComponents, LonComponents;	// Latitude, Longitude expressed in deg/min/sec
	} GeoLocation;
	struct LensInfo_t{			// Lens information
		double FStopMin;                // Min aperture (f-stop)
		double FStopMax;                // Max aperture (f-stop)
		double FocalLengthMin;          // Min focal length (mm)
		double FocalLengthMax;          // Max focal length (mm)
		double FocalPlaneXResolution;   // Focal plane X-resolution
		double FocalPlaneYResolution;   // Focal plane Y-resolution
		std::string Make;               // Lens manufacturer
		std::string Model;              // Lens model
	} LensInfo;

	EXIFInfo(){
		clear();
	}
};
EXIFInfo read(const std::string &filepath);
}
#define PARSE_EXIF_SUCCESS                    0
#define PARSE_EXIF_ERROR_NO_JPEG              1982
#define PARSE_EXIF_ERROR_NO_EXIF              1983
#define PARSE_EXIF_ERROR_UNKNOWN_BYTEALIGN    1984
#define PARSE_EXIF_ERROR_CORRUPT              1985
#endif
