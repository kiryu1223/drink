package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;
import lombok.Data;

@Data
@Getter
@Setter
public class SalesByQuarter {
    private int id;
    private int year;
    private String quarter;
    private int amount;
}
