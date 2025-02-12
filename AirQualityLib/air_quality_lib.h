#pragma once                           // Include this header only once

#include <fstream>                     // ifstream class
#include <sys/stat.h>                  // stat function and struct
#include <ctime>                       // Import the ctime library

using namespace std;                   // Allows using string directly instead of std::string

namespace devtitans::airquality {       // airquality namespace

    typedef struct Sds011 {
        int pm2_5;
        int pm10;
        bool isValid;
    } Sds011;

    typedef struct Mq9 {
        int sensorVolt;
        int rsGas;
        int ratio;
    } Mq9;

    typedef struct AirQualityData {
        Sds011 sds011;
        Mq9 mq9;
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
         *     int pm2_5;
         *     int pm10;
         *     bool isValid;
         */
        Sds011 getSds011();

        /**
         * Accesses data from the MQ9 sensor.
         *
         * Returned values:
         *     int sensor_volt;
         *     int RS_gas;
         *     int ratio;
         */
        Mq9 getMq9();

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
        char dirPath[] = "/sys/kernel/airquality/sensor";            // File where the driver writes the sensor data
};

} // namespace
