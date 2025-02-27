#pragma once                           // Include this header only once

#include <fstream>                     // ifstream class
#include <sys/stat.h>                  // stat function and struct
#include <ctime>                       // Import the ctime library
#include <iostream>
#include <stdio.h>
#include <vector>
#include <log/log.h>                   // LogCat


using namespace std;                   // Allows using string directly instead of std::string

namespace devtitans::airquality {       // airquality namespace

typedef struct SDS011 {
    int pm2_5;
    int pm10;
} SDS011;

typedef struct MQ9 {
    int voltage;
    int resistance;
    int ratio;
    int concentration;
} MQ9;

typedef struct DHT11 {
	int temperature;
	int humidity;
} DHT11;

typedef struct AirQualityData {
    SDS011 sds011;
    MQ9 mq9;
    DHT11 dht11;
} AirQualityData;

class AirQuality {
    protected:
        AirQuality();
        static AirQuality* airqualityInstance;

    public:
        /**
         * Singletons should not be cloneable.
         */
        AirQuality(AirQuality &other) = delete;

        /**
         * Singletons should not be assignable.
         */
        void operator=(const AirQuality &) = delete;

        /**
         * This static method controls access to the singleton instance.
         * On the first call, it creates a singleton object and stores it
         * in the static field. On subsequent calls, it returns the existing
         * object stored in the static field.
         */
        static AirQuality *GetInstance();
        
        /**
         * Checks if the directory dirPath exists. If it exists,
         * the device is connected via USB.
         * 
         * Returns:
         *      0: device not found
         *      1: success
         */
        int connect();

        /**
         * Accesses data from the SDS011 sensor.
         *
         * Returned values:
         * [
         *     int pm2_5,
         *     int pm10,
         * ]
         */
        vector<int> getSDS011();

        /**
         * Accesses data from the MQ9 sensor.
         *
         * Returned values:
         * [
         *     int voltage,
         *     int resistance,
         *     int ratio,
         *     int concentration,
         * ]
         */
        vector<int> getMQ9();

        /**
         * Accesses data from the DHT11 sensor.
         *
         * Returned values:
         * [
         *     int temperature,
         *     int humidity,
         * ]
         */
        vector<int> getDHT11();

    private:
        /**
         * Reads data from the file and updates the cache variable.
         */ 
        void readFileValue();

        /**
         * Checks if the difference between the current reading timestamp and
         * the timestamp of the last reading is greater than the timestamp_delta.
         * If true, calls the readFileValue method to perform a new reading and
         * update the cache variable.
         */ 
        void validateCache();

        AirQualityData sensorData;                                   // Attribute that stores the last reading data
        time_t timestampLastUpdate;                                  // Timestamp of the last reading
        int timestampDelta = 5;                                      // Minimum time in seconds that must pass between readings to update the cache
        string dirPath = "/sys/kernel/airquality"; 
        string fileName = "sensor";                             // File where the driver writes the sensor data
};

} // namespace
