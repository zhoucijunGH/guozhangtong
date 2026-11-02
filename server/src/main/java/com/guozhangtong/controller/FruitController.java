package com.guozhangtong.controller;

import com.guozhangtong.common.Result;
import com.guozhangtong.controller.dto.FruitRequest;
import com.guozhangtong.entity.Fruit;
import com.guozhangtong.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 果品档案接口：查询登录即可，维护需管理员（由 Security 统一控制）。
 */
@RestController
@RequestMapping("/api/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    /** 果品列表：keyword 模糊品类/规格，enabled 过滤启停 */
    @GetMapping
    public Result<List<Fruit>> list(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer enabled) {
        return Result.ok(fruitService.list(keyword, enabled));
    }

    /** 新增果品（ADMIN） */
    @PostMapping
    public Result<Fruit> create(@Valid @RequestBody FruitRequest req) {
        return Result.ok(fruitService.create(req));
    }

    /** 修改果品（ADMIN） */
    @PutMapping("/{id}")
    public Result<Fruit> update(@PathVariable Long id, @Valid @RequestBody FruitRequest req) {
        return Result.ok(fruitService.update(id, req));
    }

    /** 删除果品（ADMIN） */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fruitService.delete(id);
        return Result.ok();
    }
}
