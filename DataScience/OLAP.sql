--Query 1 (drill down)
select count(*), E.literacy_rate_total, M.decade, M.year, M.quarter, M.name
from fact as F, education as E, month as M
where F.education_key = E.education_key and F.month_key = M.month_key
and M.decade = 2010
group by (E.literacy_rate_total, M.decade, M.year, M.quarter, M.name)
order by E.literacy_rate_total, M.decade, M.year, M.quarter, M.name

--Query 2 (roll up)
select count(*), E.public_education_spending, C.short_name, C.region, C.continent
from fact as F, education as E, country as C
where F.education_key = E.education_key and F.country_key = C.country_key
group by E.public_education_spending, rollup (C.short_name, C.region, C.continent)
order by C.short_name, C.region, C.continent

--Query 3 (slice)
select count(*), C.short_name, M.year, Q.unemployment_female, Q.unemployment_male, Q.unemployment_total
from fact as F, country as C, month as M, qualityoflife as Q
where F.country_key = C.country_key and F.month_key = M.month_key and F.quality_of_life_key = Q.quality_of_life_key
and C.short_name = 'Canada'
group by C.short_name, M.year, Q.unemployment_female, Q.unemployment_male, Q.unemployment_total
order by m.year

--Query 4 (dice)
select count(*), C.short_name, M.year, Q.safely_managed_drinking_water_services, Q.safely_managed_drinking_water_services_rural, Q.safely_managed_drinking_water_services_urban
from fact as F, country as C, month as M, qualityoflife as Q
where F.country_key = C.country_key and F.month_key = M.month_key and F.quality_of_life_key = Q.quality_of_life_key
and C.short_name in ('Madagascar', 'Peru') and M.decade = 2000
group by C.short_name, M.year, Q.safely_managed_drinking_water_services, Q.safely_managed_drinking_water_services_rural, Q.safely_managed_drinking_water_services_urban
order by M.year

--Query 5 (dice)
select count(*), C.short_name, C.region, M.year, H.immunization_hepb3, H.immunization_dpt, H.immunization_measles, H.immunization_pol3
from fact as F, country as C, month as M, health as H
where F.country_key = C.country_key and F.month_key = M.month_key and F.health_key = h.health_key
and C.region in ('Latin America & Caribbean', 'Sub-Saharan Africa') and M.decade = 2010
group by C.short_name, C.region, M.year, H.immunization_hepb3, H.immunization_dpt, H.immunization_measles, H.immunization_pol3
order by M.year, C.region, C.short_name

--Query 6 (rollup of dice)
select count(*), C.short_name, C.region, C.continent, M.year, E.name, E.description
from fact as F, country as C, month as M, event as E
where F.country_key = C.country_key and F.month_key = M.month_key and C.short_name = E.country_name and M.year = EXTRACT(YEAR from E.start_date)
and C.region in ('Latin America & Caribbean', 'Sub-Saharan Africa')
and ((E.name like '%volcano%') or (E.name like '%earthquake%') or (E.name like '%hurricane%') or (E.name like '%storm%') or (E.name like '%fire%'))
group by rollup (C.short_name, C.region, C.continent), M.year, E.name, E.description
order by M.year, C.short_name

--Query 7 (rollup of slice)
select count(*), C.short_name, C.region, C.continent, M.year, E.name, E.description
from fact as F, country as C, month as M, event as E
where F.country_key = C.country_key and F.month_key = M.month_key and C.short_name = E.country_name and M.year = EXTRACT(YEAR from E.start_date)
and ((E.name like '%volcano%') or (E.name like '%earthquake%') or (E.name like '%hurricane%') or (E.name like '%storm%') or (E.name like '%fire%'))
group by rollup (C.short_name, C.region, C.continent), M.year, E.name, E.description
order by M.year, C.short_name

--Query 8 (drilldown of slice)
select count(*), C.short_name, EXTRACT(DECADE from E.start_date) as decade, EXTRACT(YEAR from E.start_date) as year, EXTRACT(QUARTER from E.start_date) as quarter, EXTRACT(MONTH from E.start_date) as month, E.name, E.description
from fact as F, country as C, month as M, event as E
where F.country_key = C.country_key and F.month_key = M.month_key and C.short_name = E.country_name and M.year = EXTRACT(YEAR from E.start_date)
and ((E.name like '%election%') or (E.name like '%president%') or (E.name like '%protest%') or (E.name like '%President%') or (E.name like '%constitution%') or (E.name like '%Union%') or (E.name like '%sworn%'))
group by C.short_name, EXTRACT(DECADE from E.start_date), EXTRACT(YEAR from E.start_date), EXTRACT(QUARTER from E.start_date), EXTRACT(MONTH from E.start_date), E.name, E.description
order by C.short_name, EXTRACT(DECADE from E.start_date), EXTRACT(YEAR from E.start_date), EXTRACT(QUARTER from E.start_date), EXTRACT(MONTH from E.start_date), E.name, E.description

--Query 9 (drilldown of dice)
select count(*), C.short_name, EXTRACT(DECADE from E.start_date) as decade, EXTRACT(YEAR from E.start_date) as year, EXTRACT(QUARTER from E.start_date) as quarter, EXTRACT(MONTH from E.start_date) as month, E.name, E.description
from fact as F, country as C, month as M, event as E
where F.country_key = C.country_key and F.month_key = M.month_key and C.short_name = E.country_name and M.year = EXTRACT(YEAR from E.start_date)
and M.decade >= 2010
and ((E.name like '%election%') or (E.name like '%president%') or (E.name like '%protest%') or (E.name like '%President%') or (E.name like '%constitution%') or (E.name like '%Union%') or (E.name like '%sworn%'))
group by C.short_name, EXTRACT(DECADE from E.start_date), EXTRACT(YEAR from E.start_date), EXTRACT(QUARTER from E.start_date), EXTRACT(MONTH from E.start_date), E.name, E.description
order by C.short_name, EXTRACT(DECADE from E.start_date), EXTRACT(YEAR from E.start_date), EXTRACT(QUARTER from E.start_date), EXTRACT(MONTH from E.start_date), E.name, E.description


--Query 10 (iceberg)
SELECT C.short_name, M.year, P.urban_population_growth_annual_percentage
FROM Fact as F, Population as P, country as C, month as M
WHERE F.country_key = C.country_key and F.month_key = M.month_key and F.population_key = P.population_key
and C.short_name = 'Ethiopia'
ORDER BY P.urban_population_growth_annual_percentage DESC
limit 10

--Query 11(windowing)
SELECT C.short_name, M.year, E.literacy_rate_male, avg(E.literacy_rate_male)
OVER (PARTITION BY C.short_name) as P
FROM fact as F, country as C, month as M, education as E
WHERE F.country_key = C.country_key and F.month_key = M.month_key and F.education_key = E.education_key
AND M.year > 2014 and M.year < 2020
GROUP BY C.short_name, M.year, E.literacy_rate_male
ORDER BY P

--Query 12 (Window clause)
SELECT C.short_name, M.year, AVG(H.hospital_beds_per_1000) OVER W AS movavg
FROM fact as F, country as C, month as M, health as H
WHERE F.country_key = C.country_key and F.month_key = M.month_key and F.health_key = H.health_key
AND C.short_name = 'Canada'
WINDOW W AS (PARTITION BY C.short_name
	ORDER BY M.year
	RANGE BETWEEN 1 PRECEDING
	AND 1 FOLLOWING)
