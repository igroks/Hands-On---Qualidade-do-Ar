#include "air_quality_lib.h"

using namespace std;                                   // Permite usar string, ifstream diretamente ao invés de std::string

namespace devtitans::airquality {                       // Entra no pacote devtitans::airquality

AirQuality::AirQuality() {
    this->readFileValue();
}

int AirQuality::connect() {
    struct stat dirStat;
    if (stat(this->dirPath, &dirStat) == 0)
        if (S_ISDIR(dirStat.st_mode))
            return 1;                                  // Se o diretório existir, retorna 1

    return 0                           // Dispositivo não encontrado
}

void AirQuality::readFileValue() {
    int connected = this->connect();

    if (connected == 1) {                         // Conectado. Vamos solicitar o valor ao dispositivo
        ifstream file(this->dirPath);                     // Abre o arquivo do módulo do kernel

        if (file.is_open()) {                           // Verifica se o arquivo foi aberto com sucesso
            file >> this->sensor_data;                             // Lê dados do arquivo
            file.close();
            time(&this->timestamp_last_update);
        }
    }
}

void AirQuality::validateCache() {
    time_t timestamp_now;
    time(&timestamp_now);

    int diff = difftime(timestamp_now, this->timestamp_last_update);

    if (diff > this->timestamp_delta) {
        this->readFileValue();
    }
}

Sds011 AirQuality::getSds011() {
    this->validateCache();
    return this->sensor_data.sds011;
}

Mq9 AirQuality::getMq9() {
    this->validateCache();
    return this->sensor_data.mq9;
}

} // namespace