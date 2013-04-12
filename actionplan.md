# Action Plan

## 1. Process Data

### Summary

1. Export `movielens.txt` from [Movie Lens](http://movielens.umn.edu/).
1. Populate JavaScript array of IMDb IDs from this raw file using `parse.py`. Array is stored in `movie_ids.js`.
1. Get movie data from IMDb via an unofficial [IMDb API](http://imdbapi.org/) using `imdb.htm`. Due to API request limits, successive iterations of discrete subsets were generated and stored in `movie_data.txt`. `bookkeeping.js` was also continuously updated during this process. This object enables us to understand the data set feature value and missing value distribution.
1. Run `FixMistakes.java` to take care of some initial data issues such as incorrect and duplicate feature values. Output is stored in `movie_data_revision1.txt`.
1. Run `FixNulls.java` to adjust for the cases in which an example did not contain a release day. Output is stored in `movie_data_revision2.txt`.
1. Run `PostProcess.java` to generate two data sets: `clean_data.txt` and `noisy_data_revision1.txt`. Examples with no year, writers, actors or directors are discarded. Likewise, examples with more than five missing feature values are discarded. This latter choice reflects the missing value distribution outlined below.

### Replacing Missing Values

id | feature          | fix
--:| ---------------- | -------------------
0  | id               | -
1  | imdb_id          | -
2  | rating           | standard average		
3  | rating_count     | standard average
4  | actors           | scrap
5  | directors        | scrap
6  | writers          | scrap
7  | genres           | 'Drama'
8  | language         | 'English'
9  | country          | 'USA'
10 | mpaa_rating      | 'unrated'
11 | release_year     | scrap
12 | release_month    | 10
13 | weekend          | 1
14 | runtime          | standard average

### Missing Value Distribution

Computed via `FixNulls.java`.

values | count
---:| ---
1 | 4394
2 | 1112
3 | 848
4 | 552
5 | 308
6 | 130
7 | 44
8 | 10
9 | 7
10 | 1

### Weekend Distribution

weekend | count | percent
---| --- | ---
yes | 10223 | 63%
no | 6013 | 37%

### Averages

feature | average
--- | ---
rating | 6.36890402893429
rating_count | 15274
runtime | 103

## 2. Build Models

### Baseline

Simply predicts the average value (6.3689) for all examples in the data set. This calculation is implemented in `model-tree/src/Evaluate.java`.

dataset | root-mean-square | normalized root-mean-square
--- | --- | ---
clean | 1.0762702439836687 | 0.045402246604460475
noisy | 1.0450910855264475 | 0.04408695999395767

### Model Tree
### Neural Network

## 3. Evaluate