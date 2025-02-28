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
    if (stat(this->dirPath.c_str(), &dirStat) == 0) {
        if (S_ISDIR(dirStat.st_mode))
            return 1;                                  // If the directory exists, return 1
    }
    return 0;                                           // Device not found
}

void AirQuality::readFileValue() {
    int connected = this->connect();
    int pm2_5, pm10, isValid, voltage, resistance, ratio, concentration, temperature, humidity;
    string ans;

    if (connected == 1) {                                                 // Device is connected, let's read the value
        ifstream file(this->dirPath + "/" + this->fileName);              // Open the file from the kernel module

        if (file.is_open()) {                                             // Check if the file opened successfully
            getline(file, ans);                                           // Read data into the sensor data                     
	        sscanf(ans.c_str(), "%d %d %d %d %d %d %d %d %d", &pm2_5, &pm10, &isValid, &voltage, &resistance, &ratio, &concentration, &temperature, &humidity);
            
            this->sensorData.sds011.pm2_5 = pm2_5;
            this->sensorData.sds011.pm10 = pm10;

            this->sensorData.mq9.voltage = voltage;
            this->sensorData.mq9.resistance = resistance;
            this->sensorData.mq9.ratio = ratio;
            this->sensorData.mq9.concentration = concentration;

            this->sensorData.dht11.temperature = temperature;
            this->sensorData.dht11.humidity = humidity;

            file.close();
            time(&this->timestampLastUpdate);            // Update the timestamp of the last read
        } else {
            // Log or handle error if file cannot be opened
            cerr << "Error: Unable to open the sensor data file at " << this->dirPath << endl;
            ALOG(LOG_ERROR, "Airquality", "Error: Unable to open the sensor data file");
        }
    } else {
        // Log or handle error if device is not connected
        cerr << "Error: Sensor device not connected." << endl;
        ALOG(LOG_ERROR, "Airquality", "Error: Sensor device not connected.");
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

vector<int> AirQuality::getSDS011() {
    vector<int> vetor;

    this->validateCache();  // Ensure cache is validated before returning the data
    vetor.push_back(this->sensorData.sds011.pm2_5);
    vetor.push_back(this->sensorData.sds011.pm10);

    return vetor;
}

vector<int> AirQuality::getMQ9() {
    vector<int> vetor;

    this->validateCache();  // Ensure cache is validated before returning the data
    vetor.push_back(this->sensorData.mq9.voltage);
    vetor.push_back(this->sensorData.mq9.resistance);
    vetor.push_back(this->sensorData.mq9.ratio);
    vetor.push_back(this->sensorData.mq9.concentration);

    return vetor;
}

vector<int> AirQuality::getDHT11() {
    vector<int> vetor;

    this->validateCache();  // Ensure cache is validated before returning the data
    vetor.push_back(this->sensorData.dht11.temperature);
    vetor.push_back(this->sensorData.dht11.humidity);

    return vetor;
}

} // namespace devtitans::airquality
