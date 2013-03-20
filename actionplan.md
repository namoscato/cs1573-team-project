# Action Plan

## 1. Post-Process Data

### Replacing Missing Values

id | feature		fix
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
11 | release_year     | ?
12 | release_month    | 10
13 | month_partition  | ?
14 | day_of_week      | ?
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

## 2. Build SFS

## 3. Build Models

### Model Tree
### Neural Network

## 4. Evaluate