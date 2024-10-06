FROM openjdk:17-slim

# Instalacja curl, unzip, zip
RUN apt-get update && apt-get install -y curl unzip zip

# Instalacja SDKMAN! i Kotlina
RUN curl -s https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"

# Ustawienie ścieżki SDKMAN! jako zmiennej środowiskowej
ENV SDKMAN_DIR="/root/.sdkman"
ENV PATH="${SDKMAN_DIR}/candidates/kotlin/current/bin:${PATH}"

# Ustawienie katalogu roboczego
WORKDIR /app

# Komenda uruchomienia powłoki w kontenerze (opcjonalnie)
CMD ["/bin/bash"]
