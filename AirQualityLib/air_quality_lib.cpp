#include "air_quality_lib.h"

using namespace std;                                   // Allows using string, ifstream directly instead of std::string

namespace devtitans::airquality {                       // Inside the devtitans::airquality namespace

AirQuality* AirQuality::airqualityInstance = nullptr;

AirQuality::AirQuality() {
    this->readFileValue();
}

AirQuality *AirQuality::GetInstance() {
    /**
     * This is a safer way to create an instance. 
     * Using new directly can be dangerous if two threads try to create the instance at the same time.
     */
    if(airqualityInstance == nullptr){
        airqualityInstance = new AirQuality();
    }
    return airqualityInstance;
}

int AirQuality::connect() {
    struct stat dirStat;
    if (stat(this->dirPath, &dirStat) == 0) {
        if (S_ISDIR(dirStat.st_mode))
            return 1;                                  // If the directory exists, return 1
    }
    return 0;                                           // Device not found
}

void AirQuality::readFileValue() {
    int connected = this->connect();

    if (connected == 1) {                                 // Device is connected, let's read the value
        ifstream file(this->dirPath);                     // Open the file from the kernel module

        if (file.is_open()) {                             // Check if the file opened successfully
            file >> this->sensorData;                    // Read data into the sensor data
            file.close();
            time(&this->timestampLastUpdate);            // Update the timestamp of the last read
        } else {
            // Log or handle error if file cannot be opened
            cerr << "Error: Unable to open the sensor data file at " << this->dirPath << endl;
        }
    } else {
        // Log or handle error if device is not connected
        cerr << "Error: Sensor device not connected." << endl;
    }
}

void AirQuality::validateCache() {
    time_t timestampNow;
    time(&timestampNow);

    int diff = difftime(timestampNow, this->timestampLastUpdate);  // Difference in seconds

    if (diff > this->timestampDelta) {  // If the cache is outdated, read the values again
        this->readFileValue();
    }
}

Sds011 AirQuality::getSds011() {
    this->validateCache();  // Ensure cache is validated before returning the data
    return this->sensorData.sds011;
}

Mq9 AirQuality::getMq9() {
    this->validateCache();  // Ensure cache is validated before returning the data
    return this->sensorData.mq9;
}

} // namespace devtitans::airquality
