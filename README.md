Matching company entities with company profiles [WIP]
===============================================

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
