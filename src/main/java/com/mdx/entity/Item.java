package com.mdx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
public class Item  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "用户名不可为空")
	private String name;
	@NotNull(message = "价格不可为空")
	private java.math.BigDecimal price;

	@NotNull(message = "生产日期不可为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date productionDate;

	@NotBlank(message = "描述不可为空")
	private String description;

	private String pic;

	private java.util.Date created;

}
