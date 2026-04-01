<template>
  <div class="record-container">
    <div class="main-content">
      <div class="filter-bar">
        <el-radio-group v-model="activeStatus" size="small" @change="handleFilterChange">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="配送中">配送中</el-radio-button>
          <el-radio-button label="已完成">已完成</el-radio-button>
          <el-radio-button label="已取消">已取消</el-radio-button>
        </el-radio-group>
      </div>
      <div>
        <template v-if="orderList.length>0">
          <div v-for=" record in orderList"
               :key="record.orderId"
               class="record-card">
            <div class="card-header">
              <div class="order-info">
                <span class="order-id">订单号:{{record.orderId}}</span>
                <el-tag type="warning" v-if="record.status==='待接单'">{{record.status}}</el-tag>
                <el-tag type="primary" v-if="record.status==='配送中'">{{record.status}}</el-tag>
                <el-tag type="success" v-if="record.status==='已完成'">{{record.status}}</el-tag>
                <el-tag type="danger" v-if="record.status==='已取消'">{{record.status}}</el-tag>
              </div>
              <div style="font-size: 13px; color:#999;">{{record.createTime}}</div>
            </div>
            <div class="card-body">
              <div class="route-info">
                <div class="station-point">
                  <div class="dot start">取</div>
                  <span>{{record.station}}</span>
                  <span class="code-badge">取件码:{{record.code}}</span>
                </div>
                <div class="divider-line"></div>
                <div class="station-point">
                  <div class="dot end">送</div>
                  <span>{{record.building}} - {{record.room }}</span>
                </div>
              </div>
              <div class="package-info">
                <div class="info-item">
                  <span class="label">规格</span>
                  <span class="val">{{record.size}}</span>
                </div>
                <div class="info-item price-box">
                  <span class="label">收入</span>
                  <span class="price">¥{{(record.totalPrice*0.7).toFixed(2)}}</span>
                </div>
              </div>
            </div>
            <div class="card-footer" v-if="record.remark">
              <div class="remark-text">
                <el-icon style="color:#e6a23c;"><CollectionTag/></el-icon>
                备注{{record.remark}}
              </div>
            </div>

          </div>
        </template>
        <el-empty v-else description="暂无接单记录"/>
      </div>
      <div v-if="orderList.length >0" style="display: flex;justify-content: center">
        <pagination
            v-show="total>0"
            :total="total"
            v-model:page="query.pageNum"
            v-model:limit="query.pageSize"
            @pagination="getList"/>

      </div>
    </div>
  </div>
</template>

<script setup>
import {ref,onMounted} from "vue";
import {selectOrderListByRiderToUserId} from "../../api/take/order.js";
import {CollectionTag} from "@element-plus/icons-vue";

const orderList = ref([])
const total = ref(0)
const activeStatus = ref('all')

const query = ref({
  pageNum:1,
  pageSize:10
})

const getList = () => {
  selectOrderListByRiderToUserId(query.value).then(res =>{
    orderList.value = res.rows
    total.value = res.total
  })
}

//根据状态筛选
const handleFilterChange = () => {
  if (activeStatus.value ==='all'){
    query.value.status = null
  }else{
    query.value.status=activeStatus.value
  }
  getList()
}

onMounted(() =>{
  getList()
})
</script>

<style scoped>
.record-container {
  min-height: calc(100vh - 70px);
  padding: 20px 0;
  display: flex;
  justify-content: center;
}

.main-content {
  width: 1100px;
  padding: 0 20px;
}

.filter-bar {
  background: #fff;
  padding: 10px 15px;
  border-radius: 12px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}

.record-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
  border: 1px solid #eee;
  transition: all 0.3s ease;
  overflow: hidden;
}

.record-card:hover {
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

.order-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.order-id {
  font-size: 12px;
  color: #666;
}

.card-body {
  padding: 50px;
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
  width: 100%;
  flex-wrap: nowrap;
}

.dot {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  font-size: 10px;
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
  height: 24px;
  background: #e4e7ed;
  margin-left: 11px;
  margin-top: -4px;
  margin-bottom: -4px;
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
  color: #67C23A;
  font-weight: bold;
}

.card-footer {
  padding: 15px 25px;
  border-top: 1px solid #f9f9f9;
  background: #fafafa;
}

.remark-text {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 5px 0;
}
</style>