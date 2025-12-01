package model;

import java.time.LocalDateTime;

/**
 * Represents the aggregated ingredient usage for a specific reporting period.
 */
public class IngredientUsageReport {
    private int ingredientId;
    private String ingredientName;
    private String unit;
    private double quantityUsed;
    private double stockBefore;
    private double stockAfter;
    private Integer processedBy;
    private String processedByName;
    private LocalDateTime processedAt;
    private String note;

    public IngredientUsageReport() {
    }

    public IngredientUsageReport(int ingredientId, String ingredientName, String unit, double quantityUsed) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.quantityUsed = quantityUsed;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(double quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public double getStockBefore() {
        return stockBefore;
    }

    public void setStockBefore(double stockBefore) {
        this.stockBefore = stockBefore;
    }

    public double getStockAfter() {
        return stockAfter;
    }

    public void setStockAfter(double stockAfter) {
        this.stockAfter = stockAfter;
    }

    public Integer getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }

    public String getProcessedByName() {
        return processedByName;
    }

    public void setProcessedByName(String processedByName) {
        this.processedByName = processedByName;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
