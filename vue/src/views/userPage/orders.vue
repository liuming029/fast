<template>
  <div class="orders-container">
    <div class="main-content">
      <el-tabs v-model="activeTab" class="order-tabs" @tab-click="handleTabClick">
        <el-tab-pane label="全部" name="all"/>
        <el-tab-pane label="待接单" name="待接单"/>
        <el-tab-pane label="配送中" name="配送中"/>
        <el-tab-pane label="已完成" name="已完成"/>
        <el-tab-pane label="已取消" name="已取消"/>
      </el-tabs>

      <div>
        <template v-if="orderList.length > 0">
          <div v-for="order in orderList"
               :key="order.orderId"
               class="order-card"
          >
            <div class="card-header">
              <div class="order-meta">
                <span>{{ order.createTime }}</span>
                <span>订单号: {{ order.orderId }}</span>
              </div>
              <div>
                <el-tag type="warning" v-if="order.status === '待接单'">{{ order.status }}</el-tag>
                <el-tag type="primary" v-if="order.status === '配送中'">{{ order.status }}</el-tag>
                <el-tag type="success" v-if="order.status === '已完成'">{{ order.status }}</el-tag>
                <el-tag type="danger" v-if="order.status === '已取消'">{{ order.status }}</el-tag>
              </div>
            </div>

            <div class="card-body">
              <div class="route-info">
                <div class="station-point">
                  <div class="dot start">取</div>
                  <span>{{ order.station }}</span>
                  <span class="code-badge">取件码: {{ order.code }}</span>
                </div>
                <div class="divider-line"/>
                <div class="station-point">
                  <div class="dot end">送</div>
                  <span>{{ order.building }} - {{ order.room }}</span>
                </div>
              </div>

              <div class="package-info">
                <div class="info-item">
                  <span class="label">规格</span>
                  <span class="val">{{ order.size }}</span>
                </div>
                <div class="info-item">
                  <span class="label">骑手</span>
                  <span class="val">{{ order.rider ? order.rider : '暂无骑手' }}</span>
                </div>
                <div class="info-item price-box">
                  <span class="label">实付</span>
                  <span class="price">¥{{ order.totalPrice }}</span>
                </div>
              </div>
            </div>

            <div class="card-footer">
              <div class="remark-text" v-if="order.remark">
                备注: {{ order.remark }}
              </div>
              <div>
                <el-button v-if="order.status === '待接单'" type="danger" @click="">
                  取消订单
                </el-button>
                <el-button v-if="order.status === '配送中'" type="success" @click="">
                  确认送达
                </el-button>
              </div>
            </div>
          </div>
        </template>

        <el-empty v-else description="暂无相关订单"/>
      </div>

      <div class="pagination-box" v-if="orderList.length > 0">
        <pagination
            v-show="total>0"
            :total="total"
            v-model:page="query.pageNum"
            v-model:limit="query.pageSize"
            @pagination="getList"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { selectMyOrderList } from "@/api/take/order.js";

const activeTab = ref('all')

const query = ref({
  pageNum: 1,
  pageSize: 10,
  status: null
})

const total = ref(0)
const orderList = ref([])

const handleTabClick = (tab) => {
  activeTab.value = tab.paneName
  query.value.status = activeTab.value === 'all' ? null : activeTab.value
  getList()
}

const getList = () => {
  selectMyOrderList(query.value).then(res => {
    // 兼容后端标准封装格式
    const data = res.data || res;
    total.value = data.total || 0;
    orderList.value = data.rows || [];
  }).catch(err => {
    console.error("请求失败：", err);
    orderList.value = [];
  });
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
/* 你的样式完全不变，无需修改 */
.orders-container {
  min-height: calc(100vh - 70px);
  padding: 30px 0;
  display: flex;
  justify-content: center;
}

.main-content {
  width: 1100px;
  padding: 0 20px;
}

:deep(.el-tabs__item.is-active) {
  color: #3AAE6E;
}

:deep(.el-tabs__active-bar) {
  background-color: #3AAE6E;
}

.order-tabs {
  margin-bottom: 20px;
}

.order-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
  border: 1px solid #eee;
  transition: all 0.3s ease;
  overflow: hidden;
}

.order-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.card-header {
  background: #fdfdfd;
  padding: 15px 25px;
  border-bottom: 1px solid #f5f5f5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-meta {
  font-size: 13px;
  color: #999;
  display: flex;
  gap: 20px;
}

.card-body {
  padding: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.route-info {
  flex: 1;
}

.station-point {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  font-weight: 500;
  color: #333;
  padding: 8px 0;
}

.dot {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.dot.start {
  background: #3AAE6E;
}

.dot.end {
  background: #ff9900;
}

.code-badge {
  background: #f0f9f4;
  color: #3AAE6E;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  margin-left: 10px;
  border: 1px solid rgba(58, 174, 110, 0.2);
}

.divider-line {
  width: 2px;
  height: 15px;
  background: #e4e7ed;
  margin-left: 11px;
  margin-top: -5px;
  margin-bottom: -5px;
}

.package-info {
  display: flex;
  gap: 40px;
  padding-left: 40px;
  border-left: 1px dashed #eee;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.info-item .label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-item .val {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.price-box .price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.price-box .price::before {
  font-size: 12px;
  margin-right: 1px;
}

.card-footer {
  padding: 15px 25px;
  border-top: 1px solid #f9f9f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remark-text {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-box {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>