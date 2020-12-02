package org.vvs.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.vvs.coupon.constant.CouponStatus;
import org.vvs.coupon.converter.CouponStatusConverter;
import org.vvs.coupon.serialization.CouponSerialize;
import org.vvs.coupon.vo.CouponTemplateSDK;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: vvshuai
 * @Description: 优惠券 用户领取的实体表
 * @Date: Created in 21:51 2020/12/2
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
@JsonSerialize(using = CouponSerialize.class)
public class Coupon {

    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** 关联优惠券模板主键（逻辑外键）*/
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    /** 领取用户 */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 优惠券码 */
    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    /** 领取时间 */
    @CreatedDate
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    /** 优惠券状态 */
    @Basic
    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    /** 用户优惠券 模板信息 */
    @Transient
    private CouponTemplateSDK templateSDK;
    
    /**
     * @Description: 返回一个无效的coupon对象
     * @return: org.vvs.coupon.entity.Coupon
     */
    public static Coupon invalidCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    /**
     * @Description:
     * @return: 
     */
    public Coupon(Integer templateId, Long userId, String couponCode,
                  CouponStatus status) {

        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }
}
