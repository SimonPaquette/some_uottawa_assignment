CREATE TABLE Month (
    month_key SERIAL PRIMARY KEY,
    name varchar(255),
    quarter int,
    year int,
    decade int
);

CREATE TABLE Country (
    country_key SERIAL PRIMARY KEY,
    short_name varchar(255) NOT NULL,
    long_name varchar(255) NOT NULL,
    region varchar(255) NOT NULL,
    continent varchar(255) NOT NULL,
	currency varchar(255),
	capital varchar(255),
	total_population int NOT NULL,
	two_alpha_code varchar(2),
income_group varchar(255),
	other_groups varchar(255),
	latest_trade_data int,
	birthrate DECIMAL(5,3))
;

CREATE TABLE Education (
    education_key SERIAL PRIMARY KEY,
    literacy_rate_female DECIMAL(5,2),
    literacy_rate_male DECIMAL(5,2),
    literacy_rate_total DECIMAL(5,2),
    public_education_spending DECIMAL(5,2),
    school_enrollment_primary_gross DECIMAL(5,2),
    school_enrollment_primary_female_gross DECIMAL(5,2),
    school_enrollment_primary_male_gross DECIMAL(5,2),
    school_enrollment_secondary_gross DECIMAL(5,2),
    school_enrollment_secondary_female_gross DECIMAL(5,2),
    school_enrollment_secondary_male_gross DECIMAL(5,2),
    school_enrollment_tertiary_gross DECIMAL(5,2),
    school_enrollment_tertiary_female_gross DECIMAL(5,2),
);

CREATE TABLE Population (
	population_key SERIAL PRIMARY Key,
	life_expectancy_female DECIMAL(5,2),
	life_expectancy_male DECIMAL(5,2),
	life_expectancy_total DECIMAL(5,2),
	net_migration varchar DECIMAL(15,1),
	population_ages_between_00_and_14_female DECIMAL(5,2),
	population_ages_between_00_and_14_male DECIMAL(5,2),
	population_ages_between_00_and_14_total DECIMAL(15,1),
	population_ages_between_15_and_64_female DECIMAL(5,2),
	population_ages_between_15_and_64_male DECIMAL(5,2),
	population_ages_between_15_and_64_total DECIMAL(15,1),
	population_ages_65_and_above_female DECIMAL(5,2),
	population_ages_65_and_above_male DECIMAL(5,2),
	population_ages_65_and_above_total DECIMAL(15,1),
	rural_population_total_percentage DECIMAL(5,2),
	rural_population_growth_annual_percentage DECIMAL(5,2),
	rural_poverty_percentage_of_rual_population DECIMAL(5,2),
	urban_population_percentage_of_total_population DECIMAL(5,2),
	urban_population_growth_annual_percentage DECIMAL(5,2)
 );

CREATE TABLE QualityOfLife (
	quality_of_life_key SERIAL PRIMARY KEY,
	basic_sanitation_services DECIMAL(5,2),
	basic_handwashing_facilities DECIMAL(5,2),
	unemployment_female DECIMAL(5,2),
	unemployment_male DECIMAL(5,2),
	unemployment_total DECIMAL(5,2),
	maternal_leave_benefits DECIMAL(5,2),
	basic_drinking_water_services DECIMAL(5,2),
	basic_sanitation_services_rural DECIMAL(5,2),
	basic_sanitation_services_urban DECIMAL(5,2),
	safely_managed_drinking_water_services DECIMAL(5,2),
	safely_managed_drinking_water_services_rural DECIMAL(5,2),
	safely_managed_drinking_water_services_urban DECIMAL(5,2)
);

CREATE TABLE health (
    health_key SERIAL PRIMARY KEY,
    domestic_gouvernement_health_expenditure_gdp DECIMAL(5,2),
    hospital_beds_per_1000 DECIMAL(6,2),
    immunization_hepb3 DECIMAL(5,2),
    immunization_dpt DECIMAL(5,2),
    immunization_measles DECIMAL(5,2),
    immunization_pol3 DECIMAL(5,2),
    number_of_surgical_procedures_per_100000 DECIMAL(8,2),
    number_of_death_infant INT,
    number_of_death_stillbirths INT,
    number_of_nurses_per_1000 DECIMAL(6,2),
    number_of_physicians_per_1000 DECIMAL(6,2),
    prevalence_of_anemia_children DECIMAL(5,2),
    prevalence_of_anemia_non_pregnant DECIMAL(5,2),
    prevalence_of_anemia_pregnant DECIMAL(5,2),
    prevalence_of_anemia_women DECIMAL(5,2),
    prevalence_of_diabetes_total DECIMAL(5,2),
    prevalence_of_hiv_male DECIMAL(5,2),
    prevalence_of_hiv_female DECIMAL(5,2),
    prevalence_of_hiv_total DECIMAL(5,2),
    prevalence_of_overweight_children DECIMAL(5,2),
    prevalence_of_overweight_male DECIMAL(5,2),
    prevalence_of_overweight_female DECIMAL(5,2),
    prevalence_of_overweight_total DECIMAL(5,2),
    adults_with_hiv INT,
    adults_newly_infected_hiv INT,
    children_with_hiv INT,
    children_newly_infected_hiv INT
);

CREATE TABLE Event (
	event_key SERIAL PRIMARY KEY,
	name varchar(255),
	description varchar(1000),
	start_date date,
	end_date date,
	start_month int CHECK (start_month >= 1 AND start_month <= 12),
	end_month int CHECK (end_month >= 1 AND end_month <= 12),
	outcome varchar(255)
);

CREATE TABLE fact (
	quality_of_life_index int CHECK (quality_of_life_index >= 1 AND quality_of_life_index <= 5),
	development_index int CHECK (development_index >= 1 AND development_index <= 3),
	human_development_index int CHECK (human_development_index >= 1 AND human_development_index <= 5),
	country_key SERIAL references country(country_key),
	month_key SERIAL references month(month_key),
	education_key SERIAL references education(education_key),
	health_key SERIAL references health(health_key),
	quality_of_life_key SERIAL references qualityoflife(quality_of_life_key),
	population_key SERIAL references population(population_key),
	event_key SERIAL references event(event_key),
	PRIMARY KEY (country_key, month_key, education_key, health_key, quality_of_life_key, population_key, event_key)
);
