-- пользователь не использует продукты типа INVEST
SELECT COUNT(*)
FROM TRANSACTIONS t
         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
WHERE t.USER_ID = ?
  AND p.TYPE = 'INVEST';