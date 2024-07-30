SELECT t0.`emp_no`, t0.`from_date`, t0.`salary`, t0.`to_date`
FROM `salaries` AS t0
         LEFT JOIN `employees` AS t1 ON t0.`emp_no` = t1.`emp_no`
WHERE t0.`emp_no` IN (SELECT t0.`emp_no`
                      FROM (SELECT t0.`birth_date`,
                                   t0.`first_name`,
                                   t0.`gender`,
                                   t0.`hire_date`,
                                   t0.`last_name`,
                                   t0.`emp_no`
                            FROM `employees` AS t0
                            LIMIT 5) AS t0)
