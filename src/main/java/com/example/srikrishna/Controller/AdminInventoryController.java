package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Service.InventoryService;

@Controller
@RequestMapping("/admin/inventory")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    public AdminInventoryController(
            InventoryService inventoryService) {

        this.inventoryService =
                inventoryService;
    }

    // =====================================================
    // INVENTORY DASHBOARD
    // =====================================================

    @GetMapping
    public String inventory(Model model) {

        model.addAttribute(
                "products",
                inventoryService.getAllProducts()
        );

        model.addAttribute(
                "totalProducts",
                inventoryService.getTotalProducts()
        );

        model.addAttribute(
                "inStockProducts",
                inventoryService.getInStockProducts()
        );

        model.addAttribute(
                "lowStockProducts",
                inventoryService.getLowStockProducts()
        );

        model.addAttribute(
                "outOfStockProducts",
                inventoryService.getOutOfStockProducts()
        );

        model.addAttribute(
                "history",
                inventoryService.getHistory()
        );

        return "admin/inventory";
    }

    // =====================================================
    // UPDATE STOCK
    // =====================================================

    @PostMapping("/update")
    public String updateStock(

            @RequestParam("productId")
            Long productId,

            @RequestParam("quantity")
            Integer quantity,

            @RequestParam("changeType")
            String changeType,

            @RequestParam(
                    value = "reason",
                    required = false
            )
            String reason,

            RedirectAttributes redirectAttributes) {

        try {

            inventoryService.updateStock(
                    productId,
                    quantity,
                    changeType,
                    reason
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Stock updated successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/inventory";
    }

    // =====================================================
    // PRODUCT HISTORY
    // =====================================================

    @GetMapping("/history/{productId}")
    public String productHistory(

            @PathVariable Long productId,

            Model model) {

        model.addAttribute(
                "history",
                inventoryService
                        .getProductHistory(productId)
        );

        model.addAttribute(
                "productId",
                productId
        );

        return "admin/inventory-history";
    }
}