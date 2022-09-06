package hellojpa;

import javax.persistence.*;

@Entity
/*
1. JOINED : 하나에서 DTYPE으로 그 외 테이블 관리, 즉 테이블 4개
2. SINGLE_TABLE : 테이블 하나로 모든 정보, 즉 테이블 1개
3. TABLE_PER_CLASS : 테이블 각각 PK, 즉 테이블 3개
 */
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
