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

Simply predicts the average rating for all examples in the data set. This calculation is implemented in `model-tree/src/Evaluate.java`.

dataset | average rating | rms | normalized rms
--- | --- | --- | ---
1000 clean | 6.3869 | 1.0452 | 0.0631
1000 noisy | 6.4241 | 1.0813 | 0.0446

### Model Tree

A model tree learning algorithm was implemented from scratch based off of M5. The input consisted of ten nominal attributes (actors, directors, writers, genres, language, country, mpaa_rating, release_year, release_month and weekend) and one numerical attribute, runtime. Many of the examples had multiple values for for a particular nominal attribute. For example, up to five actors were included as input for one movie to the actor attribute. In these cases, the particular example was included in the set of all relevent child nodes. Features were chosen based of the standard deviation of ratings adjusted for the size of each subset.

As expected, attributes such as actors, writers and directors had a relatively high cardinality.

attribute | clean | noisy
--- | --- | ---
__actor__ | 3904 | 4108
__director__ | 873 | 886
__writer__ | 1474 | 1465
__genre__ | 23 | 24
__language__ | 69 | 72
__country__ | 56 | 68
__mpaa rating__ | 7 | 7
__release year__ | 87 | 91

#### Split Actors

In an effort to decrease the effects of the large cardinality of actors, this feature was split up into three separate features: actor1, actor2 and actor3. This decision was made with the assumption that actor1 is more "popular" than actor2 and so on. Movies with less than three actors were removed.

attribute | clean | noisy
--- | --- | ---
__discarded__ | 3 | 8
__actors 1__ | 851 | 895
__actors 2__ | 905 | 945
__actors 3__ | 918 | 921

It should be noted that other nominal attributes such as writers and directors also consisted of a fairly large set of distinct values. However, these distinct sets were not as large as the actor attribute. More importantly, all movies had at most two directors and writers associated with them.

Unfortunately, splitting up the actor attribute did not improve the model.

### Neural Network

## 3. Evaluate