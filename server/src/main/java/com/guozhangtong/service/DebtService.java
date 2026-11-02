package com.guozhangtong.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guozhangtong.entity.PurchaseOrder;
import com.guozhangtong.mapper.PurchaseOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 欠款服务：按果农聚合未付收购单。
 */
@Service
public class DebtService {

    private final PurchaseOrderMapper orderMapper;

    public DebtService(PurchaseOrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 只聚合 pay_status=UNPAID，按果农分组；
     * lastDate 为该果农最近一笔未付的收购日期。
     */
    public List<Map<String, Object>> list(String keyword) {
        List<PurchaseOrder> unpaid = orderMapper.selectList(new LambdaQueryWrapper<PurchaseOrder>()
                .eq(PurchaseOrder::getPayStatus, "UNPAID")
                .and(StringUtils.hasText(keyword), w -> w
                        .like(PurchaseOrder::getFarmerName, keyword)
                        .or().like(PurchaseOrder::getFarmerPhone, keyword)));

        Map<String, Map<String, Object>> groups = new LinkedHashMap<>();
        for (PurchaseOrder o : unpaid) {
            String key = o.getFarmerName() + "#" + o.getFarmerPhone();
            Map<String, Object> g = groups.computeIfAbsent(key, k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("farmerName", o.getFarmerName());
                m.put("phone", o.getFarmerPhone());
                m.put("unpaidCount", 0L);
                m.put("unpaidAmount", BigDecimal.ZERO);
                m.put("lastDate", o.getOrderDate() == null ? null : o.getOrderDate().toString());
                return m;
            });
            g.put("unpaidCount", (Long) g.get("unpaidCount") + 1);
            BigDecimal amount = o.getAmount() == null ? BigDecimal.ZERO : o.getAmount();
            g.put("unpaidAmount", ((BigDecimal) g.get("unpaidAmount")).add(amount));
            String last = o.getOrderDate() == null ? null : o.getOrderDate().toString();
            String cur = (String) g.get("lastDate");
            if (last != null && (cur == null || last.compareTo(cur) > 0)) {
                g.put("lastDate", last);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>(groups.values());
        // 按最近未付日期倒序
        result.sort(Comparator.comparing((Map<String, Object> m) -> (String) m.get("lastDate"),
                Comparator.nullsLast(Comparator.reverseOrder())));
        return result;
    }
}
