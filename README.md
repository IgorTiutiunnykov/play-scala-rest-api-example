Matching company entities with company profiles [WIP]
===============================================

## To Run

in sbt console: run
in terminal: sbt run

It'll be two endpoints available: 
* _localhost:9000/companies_ with default limit=10
* _localhost:9000/companies/by_name _ with params:
```aidl
id
company_name
website_url
foundation_year
city
country
limit (default 10)
```

First endpoint returns all matched companies
Second - match company data with company_entities

## Info

At XING, we have different types of information about companies. In this assignment, we analyze samples
from two different tables that provide information about companies:

- `company_entities.tsv`: lists the legal company entities
- `company_profiles.tsv`: lists the company profiles on XING

Both tables have the following structure:

| Column             | Description |
|:-------------------|:------------|
| `id`               | ID of the company entity or company profile. Careful: the entity IDs are different from the profile IDs, e.g. ID 4 in `company_entities` may refer to _XING SE_ while ID 4 in `company_profiles` may refer to _Siemens_ |
| `company_name`     | name of the company |
| `website_url`      | URL that may link to the company website |
| `foundation_year`  | year when the company was founded |
| `city`             | name of the city where the company is located |
| `country`          | code of the country where the company is located |

We assume that there is a 1:n relationship between company profiles and company entities, i.e. one company profile may
refer to zero or more company entities.

**Task:** Your task is to analyze the given dataset and...

1. Design a lightweight algorithm that allows for matching company entities and company profiles. Scala or Java prefered, but it is up to you.
2. Use the ground_truth.tsv to validate your algorithm. What kind of numbers would you report?
3. Small, basic REST app to wrap your matching algorithm. It should return the matched companies given company profile data.


## Remarks

- We do not expect that you find a sophisticated, optimal solution. A lightweight, simple algorithm is perfectly fine. However, we expect that you will be able to justify why the algorithm makes sense with respect to the given data.
- You are responsible for managing your time (e.g. if you want to invest 1 hour that's fine or if you want to invest 4 hours that's also fine)
- Please submit your solution until 5 August 2019 9 AM, because we would like to discuss your solution in the week after that.

## About implementation
All files needed for this algorithm to work are in app folder, the rest is generated by Play. I used an example from Play as framework to finish this task.

Final solution matches on 100% match by normalized name or url + country. It was the best performing in terms of speed and results.


### Analyse
I tried to improve matching using Levenstein distance, here some results

|   Levenstein score           | Total matches | Missed matches | False matches |
|:-----------------------------|:--------------|:---------------|:--------------|
|100%   | 10755     | 4739     | 694    |
|95%    | 10767     | 4731     | 698    |
|90%    | 10820     | 4968     | 988    |
|80%    | 10820     | 4887     | 11081  |

#### Missed matches
After analyzing missed matches 
Levenstein score for comaparing normalized Company name in profiles and entities tsv.

| Levenstein score | Total matches Company name | Total matches Web Url      |
|:-----------------|:---------------------------|:---------------------------|
| 0.0 | 100 | 2387  |
| 0.1 | 1024 | 429    |
| 0.2 | 986 | 396   |
| 0.3 | 806  | 341   |
| 0.4 | 696  | 348   |
| 0.5 | 463  | 268   |
| 0.6 | 274  | 249   |
| 0.7 | 191  | 101   |
| 0.8 | 143  | 42   |
| 0.9  | 48   | 29    |
| 0.95 < | 8    | 149   |

#### False matches
Analyse of entries that algorithm found as matches, but they aren't in ground truth

| Levenstein score | Total matches Company name | Total matches Web Url      |
|:-----------------|:---------------------------|:---------------------------|
| 0.0 | 2 | 38  |
| 0.1 | 51 |   7  |
| 0.2 | 129 |  9 |
| 0.3 | 111  | 10   |
| 0.4 | 104  | 7   |
| 0.5 | 87  | 10   |
| 0.6 | 57  | 5   |
| 0.7 | 30  | 1   |
| 0.8 | 19  | 0   |
| 0.9  | 6   | 1    |
| 0.95 < | 98    | 606   |

So there're a lot of 'good' matches which aren't in ground truth. E.g.
```
52728	Atos IT Solutions and Services GmbH Austria	http://at.atos.net	NULL	Wien	AT
300219519	Atos IT Solutions and Services GmbH	at.atos.net	2011	Wien	AT
```
It matches 100% by name, URL city and country

###### Check on country

If I match url without checking Country:

```
total   11179   (+424)
missed  4590    (-148)
false   969     (+275)
```
There were more matched but double increase of false matches.
*So I kept check on Country*

###### Removal of .de/.at/.ch/.com

With these domain names .de/.at/.ch/.com there are these results
```
total     10622   (-133)
missed    4832    (+94)
false     654     (-40)
```
There were more missed companies with less false matches, but in total it was better result.
With removing other domains it was no such positive effect on total matches, but more false entries.

#### Notes
There are some questionable data in Ground truth: 

```
52728	Atos IT Solutions and Services GmbH Austria	http://at.atos.net	NULL	Wien	AT
27723	Atos	http://www.atos.net/deutschland	NULL	Essen	DE
```

52728 is matched with, which doesn't seem correct
```
300237765	Siemens Aktiengesellschaft Österreich	www.siemens.at	1879	Wien	AT
```
52728 is also matched with 
```
341599498	Atos IT Solutions and Services GmbH	de.atos.net/de-de/	2010	München	DE
```
Which is ok, but not with 
```
300219519	Atos IT Solutions and Services GmbH	at.atos.net	2011	Wien	AT 
```
In ground truth we say that _300219519_ is better fit for _27723_ though I'd say that _52728_ is better, since web_url and city also match and the mame is more precise.

Another example:
```aidl
26183	Otto (GmbH & Co KG)	http://www.otto.de/jobs	1949	Hamburg	DE
```
matches with
	
```aidl
343416186	ESE GmbH	www.ese.com	1934	Neuruppin	DE
```

```
389267084	Uwe Kneis		1994	Bernburg (Saale)	DE
```
I wonder how and why they match?

There more examples like this.