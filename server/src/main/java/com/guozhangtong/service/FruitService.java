package com.guozhangtong.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guozhangtong.common.BizException;
import com.guozhangtong.controller.dto.FruitRequest;
import com.guozhangtong.entity.Fruit;
import com.guozhangtong.mapper.FruitMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 果品档案服务：查询 / 维护（维护仅管理员）。
 */
@Service
public class FruitService {

    private final FruitMapper fruitMapper;

    public FruitService(FruitMapper fruitMapper) {
        this.fruitMapper = fruitMapper;
    }

    /** 果品列表：按品类/规格模糊搜索，可按启用状态过滤 */
    public List<Fruit> list(String keyword, Integer enabled) {
        LambdaQueryWrapper<Fruit> wrapper = new LambdaQueryWrapper<Fruit>()
                .eq(enabled != null, Fruit::getEnabled, enabled)
                .and(StringUtils.hasText(keyword), w -> w
                        .like(Fruit::getName, keyword)
                        .or().like(Fruit::getSpec, keyword))
                .orderByDesc(Fruit::getId);
        return fruitMapper.selectList(wrapper);
    }

    /** 新增果品 */
    public Fruit create(FruitRequest req) {
        Fruit fruit = new Fruit();
        apply(fruit, req);
        fruit.setEnabled(req.getEnabled() == null ? 1 : req.getEnabled());
        fruitMapper.insert(fruit);
        return fruit;
    }

    /** 修改果品 */
    public Fruit update(Long id, FruitRequest req) {
        Fruit fruit = fruitMapper.selectById(id);
        if (fruit == null) {
            throw new BizException(404, "果品不存在");
        }
        apply(fruit, req);
        if (req.getEnabled() != null) {
            fruit.setEnabled(req.getEnabled());
        }
        fruitMapper.updateById(fruit);
        return fruit;
    }

    /** 停用/删除果品（物理删除，档案仅 6 条且订单存冗余快照） */
    public void delete(Long id) {
        if (fruitMapper.selectById(id) == null) {
            throw new BizException(404, "果品不存在");
        }
        fruitMapper.deleteById(id);
    }

    private void apply(Fruit fruit, FruitRequest req) {
        fruit.setName(req.getName().trim());
        fruit.setSpec(req.getSpec().trim());
        fruit.setPrice(req.getPrice());
        fruit.setUnit(req.getUnit().trim());
    }
}
