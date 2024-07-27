SELECT t0.`alias`, t0.`createTime`, t0.`id`, t0.`stars`, t0.`title`
FROM `t_topic` AS t0;
SELECT DISTINCT t0.`id0000`
FROM (SELECT DISTINCT t0.`stars` AS `id0000`, t0.`id` AS `stars0000`
      FROM `t_topic` AS t0
      WHERE t0.`stars` >= ?
        AND t0.`title` <> ?
      ORDER BY t0.`id` ASC, t0.`createTime` DESC) AS t0
SELECT DISTINCT t0.`stars`
FROM `t_topic` AS t0
WHERE t0.`stars` >= ?
  AND t0.`title` <> ?
ORDER BY t0.`id` ASC, t0.`createTime` DESC
SELECT DISTINCT t1.`stars` AS `id0000`, t0.`id` AS `stars0000`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
ORDER BY t0.`id` DESC, t1.`createTime` ASC
SELECT t0.`stars` AS `id0000`, t0.`id` AS `stars0000`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
GROUP BY t0.`stars`, t0.`id`
HAVING t0.`stars` > ?
ORDER BY t0.`id` DESC, t0.`stars` ASC
SELECT t0.`stars` AS `a00`, SUM(t0.`stars`) AS `b00`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
GROUP BY t0.`stars`
HAVING t0.`stars` > ?
   AND COUNT(t1.`createTime`) <> ?
ORDER BY t0.`stars` DESC
SELECT t0.`stars`, t0.`title`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
SELECT t0.`stars` AS `k1`, t0.`id` AS `k2`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
GROUP BY t0.`stars`, t0.`id`
ORDER BY t0.`id` DESC, t0.`stars` ASC
SELECT t0.`stars`
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE t0.`stars` >= ?
   OR t1.`title` <> ?
GROUP BY t0.`stars`
ORDER BY t0.`stars` DESC
SELECT COUNT(t0.`id`)
FROM `t_topic` AS t0
WHERE CONVERT(SIGNED, t0.`id`) > ?
SELECT GROUP_CONCAT(t0.`id` SEPARATOR ?)
FROM `t_topic` AS t0
WHERE CONVERT(SIGNED, t0.`id`) > ?
SELECT GROUP_CONCAT(t0.`id` SEPARATOR ?)
FROM `t_topic` AS t0
WHERE t0.`stars` IN (?, ?, ?, ?, ?)
SELECT GROUP_CONCAT(t0.`id` SEPARATOR ?)
FROM `t_topic` AS t0
WHERE ? LIKE CONCAT('%', t0.`title`, '%')
   OR ? LIKE CONCAT(t0.`title`, '%')
   OR ? LIKE CONCAT('%', t0.`title`)
SELECT ADDDATE(t0.`createTime`, INTERVAL ? DAYS)
FROM `t_topic` AS t0
SELECT t0.`alias`, t0.`createTime`, t0.`id`, t0.`stars`, t0.`title`
FROM `t_topic` AS t0
WHERE t0.`stars` BETWEEN ? AND ?
SELECT ?
FROM `t_topic` AS t0
         LEFT JOIN `t_topic` AS t1 ON t0.`id` = t1.`id`
WHERE EXISTS (SELECT 1 FROM `Top` AS t2 WHERE t0.`title` = CHAR(t2.`stars`))
  AND EXISTS (SELECT 1 FROM `Top` AS t2 WHERE t1.`title` = CHAR(t2.`stars`))
SELECT ?
FROM `t_topic` AS t0
WHERE EXISTS (SELECT 1 FROM `Top` AS t1 WHERE t0.`id` = t1.`title`)
SELECT t0.`alias`, t0.`createTime`, t0.`id`, t0.`stars`, t0.`title`
FROM `t_topic` AS t0
WHERE t0.`id` = ? AND t0.`id` = ?
   OR t0.`id` = ?
SELECT t0.`alias`, t0.`createTime`, t0.`id`, t0.`stars`, t0.`title`
FROM `t_topic` AS t0
WHERE NOT EXISTS (SELECT 1 FROM `Top` AS t1 WHERE t0.`id` <> t1.`title`)
  AND EXISTS (SELECT 1 FROM `Top` AS t1 WHERE t0.`stars` >= t1.`stars`) class java.lang.String
class io.github.kiryu1223.drink.pojos.Gender
SELECT t0.`dept_no` AS `deptId`, t2.`dept_name` AS `deptName`, AVG(t1.`salary`) AS `avgSalary`
FROM `dept_emp` AS t0
         INNER JOIN `salaries` AS t1 ON t0.`emp_no` = t1.`emp_no`
         INNER JOIN `departments` AS t2 ON t0.`dept_no` = t2.`dept_no`
WHERE t0.`dept_no` = ?
  AND t1.`to_date` = ?
GROUP BY t0.`dept_no`, t2.`dept_name`