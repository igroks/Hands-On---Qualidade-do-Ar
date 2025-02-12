#pragma once                           // Inclui esse cabeçalho apenas uma vez

#include <fstream>                     // Classe ifstream
#include <sys/stat.h>                  // Função e struct stat
#include <ctime>                       // Import the ctime library

using namespace std;                   // Permite usar string diretamente ao invés de std::string

namespace devtitans::airquality {       // Pacote Smartlamp

    typedef struct Sds011 {
        int pm2_5;
        int pm10;
        bool isValid;
    } Sds011;

    typedef struct Mq9 {
        int sensor_volt;
        int RS_gas;
        int ratio;
    } Mq9;

    typedef struct AirQualityData {
        Sds011 sds011;
        Mq9 mq9;
    } AirQualityData;

class AirQuality {    
    public:
        /**
         * Verifica se o diretório /sys/kernel/airquality/sensor existe. Se existir
         * o dispositivo AirQuality está conectado via USB.
         *
         * Retorna:
         *      0: dispositivo não encontrado
         *      1: sucesso
         */
        int connect();

        /**
         * Acessa dados recentes do sensor SDS 011.
         */
        Sds011 getSds011();

         /**
         * Acessa dados recentes do sensor MQ9.
         */
        Mq9 getMq9();

    private:
        void readFileValue();
        void validateCache();

        AirQualityData sensor_data;
        time_t timestamp_last_update;
        int timestamp_delta = 5;                                     // Tempo minimo em segundos que deve existir entre as leituras para atualizar a cache
        char dirPath[] = "/sys/kernel/airquality/sensor";
};

} // namespace