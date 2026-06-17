# Dockerized BSBM

This is the Dockerized version of the Berlin SPARQL Benchmark.

## Links
- Original work : http://wbsg.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/
- Sources : https://github.com/VCityTeam/BSBM
- Images published on [Docker hub](https://hub.docker.com/r/vcity/bsbm).

## Usage
```bash
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm generate [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm generate-n [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm qualification [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm testdriver [args]
```

### generate-n options
The `generate-n` command accepts the following arguments:
- `--versions` or `-v` (required): Number of dataset versions to generate
- `--products` or `-p` (default: 100): Base product count. In linear mode it is the additive base (version `i` has `products + i × step`, so the first version is `products + step`, not `products`). In easing mode it is the exact count of the first version.
- `--step` or `-s` (default: 1000): Product increment per version (linear mode only)
- `--target` or `-t` (optional): Final product count (last version). Providing it enables **easing mode** (see below). When set, `--step` is ignored.
- `--easing` or `-e` (default: linear): Easing curve used in easing mode. Accepts a named curve or a raw `awk` expression of `t`.
- `--format` or `-f` (default: ttl): Output format (nt, ttl, trig, xml, sql, virt, monetdb)
- `--var` (default: 0): Variability percentage (0-100). Controls the percentage of products that change between versions. When set to a value greater than 0, each version generates an update dataset containing the specified percentage of products as changes.

#### Product evolution: linear vs. easing
By default `generate-n` grows the product count **linearly**: version `i` contains `products + i × step` products.

When you pass `--target`, the product count is instead **interpolated from `--products` to `--target`** across the versions following an easing curve. Easing mode requires `--versions >= 2`. For version `i` of `N` the progress is `t = (i-1)/(N-1)` and the count is `round(products + easing(t) × (target − products))`. The first version always equals `--products` and the last always equals `--target`; the chosen curve only changes how the intermediate versions are distributed. Descending ranges (`target < products`) are supported.

`--easing` accepts either a named curve or any raw `awk` expression of `t` (the version progress in `[0, 1]`). Available named curves:

```
linear
easeInSine     easeOutSine     easeInOutSine
easeInQuad     easeOutQuad     easeInOutQuad
easeInCubic    easeOutCubic    easeInOutCubic
easeInQuart    easeOutQuart    easeInOutQuart
easeInQuint    easeOutQuint    easeInOutQuint
easeInExpo     easeOutExpo     easeInOutExpo
easeInCirc     easeOutCirc     easeInOutCirc
easeInBack     easeOutBack     easeInOutBack
easeInElastic  easeOutElastic  easeInOutElastic
easeInBounce   easeOutBounce   easeInOutBounce
```

The `Back` and `Elastic` curves intentionally **overshoot** the `[products, target]` range, so intermediate versions may dip below `--products` or rise above `--target` (counts are always clamped to at least 1). The first and last versions still equal `--products` and `--target` exactly.

Any value that is not a known curve name is treated as a custom `awk` expression of `t`, so you can supply your own curve (e.g. `-e 't*t*t'`, which is equivalent to `easeInCubic`). A custom expression must reference `t`; a bare word or a constant is rejected to avoid silently flattening every version.

Examples:
```bash
# 5 versions ramping from 100 to 5000 products along an ease-in/out sine curve
docker run -v "$PWD:/app/data" vcity/bsbm generate-n -v 5 -p 100 -t 5000 -e easeInOutSine

# Front-loaded growth via a custom expression
docker run -v "$PWD:/app/data" vcity/bsbm generate-n -v 5 -p 100 -t 5000 -e 'sqrt(t)'
```

### Diff output files
For each version >= 2, `generate-n` automatically computes the RDF diff between consecutive versions and outputs:
- `dataset-X_additions.nt`: triples present in version X but not in version X-1
- `dataset-X_deletions.nt`: triples present in version X-1 but not in version X

These files are always generated in N-Triples format regardless of the `--format` option, since they are computed by comparing sorted N-Triples representations of each version.

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