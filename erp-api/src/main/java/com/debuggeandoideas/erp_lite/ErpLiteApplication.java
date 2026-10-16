package com.debuggeandoideas.erp_lite;

import com.debuggeandoideas.erp_lite.enums.CatalogType;
import com.debuggeandoideas.erp_lite.queries.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ErpLiteApplication implements CommandLineRunner {

    @Autowired
    private FindCatalogByTypeQuery findCatalogByTypeQuery;

    @Autowired
    private FindCatalogItemByCodeQuery findCatalogItemByCodeQuery;

    @Autowired
    private FindCatalogItemsByTypeQuery findCatalogItemsByTypeQuery;

    @Autowired
    private FindProductActiveQuery findProductActiveQuery;

    @Autowired
    private FindProductByCategory findProductByCategory;

    @Autowired
    private FindProductByIdQuery findProductByIdQuery;

    @Autowired
    private FindProductBySkuQuery findProductBySkuQuery;

    @Autowired
    private FindProductByTextQuery findProductByTextQuery;

    public static void main(String[] args) {
        SpringApplication.run(ErpLiteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(findCatalogByTypeQuery.execute(CatalogType.PRODUCT_CATEGORIES));
        System.out.println("-------------------------");

        System.out.println(findCatalogItemByCodeQuery.execute(CatalogType.PRODUCT_CATEGORIES, "ELECTRONICS"));
        System.out.println("-------------------------");

        System.out.println(findCatalogItemsByTypeQuery.execute(CatalogType.ORDER_STATUSES));
        System.out.println("-------------------------");

        System.out.println(findProductActiveQuery.execute());
        System.out.println("-------------------------");

        System.out.println(findProductByCategory.execute("cat-electronics"));
        System.out.println("-------------------------");

        System.out.println(findProductByIdQuery.execute("11111111-1111-1111-1111-111111111111"));
        System.out.println("-------------------------");

        System.out.println(findProductBySkuQuery.execute("LAPTOP-001"));
        System.out.println("-------------------------");

        System.out.println(findProductByTextQuery.execute("laptop"));
        System.out.println("-------------------------");
    }
}
