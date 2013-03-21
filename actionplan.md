# Action Plan

## 1. Post-Process Data

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
13 | month_partition  | most common occurrence
14 | day_of_week      | most common occurrence
15 | runtime          | standard average

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

### Day of Week Distribution

day | count
---:| ---
0 | 2357
1 | 2340
2 | 2359
3 | 2347
4 | 2238
5 | 2259
6 | 2336

### Month Partition Distribution

partition | count
---:| ---
0 | 5033
1 | 5557
2 | 5646

## 2. Build SFS

## 3. Build Models

### Model Tree
### Neural Network

## 4. Evaluate