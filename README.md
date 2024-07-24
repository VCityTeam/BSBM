# Dockerized BSBM

This is the Dockerized version of the Berlin SPARQL Benchmark.

## Links
- Original work : http://wbsg.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/
- Sources : https://github.com/VCityTeam/BSBM
- Images published on [Docker hub](https://hub.docker.com/r/vcity/bsbm).

## Usage
```bash
docker run -v "$PWD:/data" vcity/bsbm generate [args]
docker run -v "$PWD:/data" vcity/bsbm generate-n [args]
docker run -v "$PWD:/data" vcity/bsbm qualification [args]
docker run -v "$PWD:/data" vcity/bsbm testdriver [args]
```

If you want more information about the different arguments, please refer to the original documentation.

```bash
docker run vcity/bsbm generate -help
docker run vcity/bsbm generate-n -help
docker run vcity/bsbm qualification -help
docker run vcity/bsbm testdriver -help
```

$PWD is the directory where the data will be stored.
You can change it to any directory you want.

## Modifications from source:
**Dockerfile:**
- Added new authors
- Dockerized the benchmark

**entrypoint.sh**
- Added a new entrypoint script to run the benchmark