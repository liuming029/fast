<template>
  <div class="takeout-container">
    <div class="main-layout">
      <div class="form-section">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <!-- 包裹信息  -->
          <div class="form-card">
            <div class="card-title">
              <el-icon>
                <Box/>
              </el-icon>
              包裹信息
            </div>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="快递站点" prop="stationId">
                  <el-select v-model="form.stationId" placeholder="请选择站点" style="width: 100%;" size="large">
                    <el-option v-for="station in stationList"
                               :key="station.stationId"
                               :label="station.name"
                               :value="station.stationId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="取件码" prop="code">
                  <el-input v-model="form.code" placeholder="例: 3-2056 或 A123" size="large"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="包裹规格" prop="sizeId">
              <div class="size-selector">
                <div v-for="item in sizeList"
                     :key="item.sizeId"
                     class="size-card"
                     :class="{ active: form.sizeId === item.sizeId }"
                     @click="form.sizeId = item.sizeId"
                >
                  <div class="size-icon">📦</div>
                  <div>
                    <div class="size-name">{{ item.name }}</div>
                    <div class="size-desc">{{ item.description }}</div>
                  </div>
                  <div class="size-price">+¥{{ item.price }}</div>
                </div>
              </div>
            </el-form-item>
          </div>

          <!-- 配送地址  -->
          <div class="form-card">
            <div class="card-title">
              <el-icon>
                <Location/>
              </el-icon>
              配送地址
            </div>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="宿舍楼栋" prop="buildingId">
                  <el-select v-model="form.buildingId" placeholder="请选择楼栋" style="width: 100%;" size="large">
                    <el-option v-for="building in buildingList"
                               :key="building.buildingId"
                               :label="building.name"
                               :value="building.buildingId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="寝室号/联系人" prop="room">
                  <el-input v-model="form.room" placeholder="例: 302-张三" size="large"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="备注需求" prop="remark">
              <el-input v-model="form.remark"
                        placeholder="例如：放在门口、不要敲门等"
                        maxlength="50"
                        show-word-limit
              />
            </el-form-item>
          </div>
        </el-form>
      </div>

      <div class="summary-section">
        <div class="receipt-card">
          <div class="receipt-header">
            <h2 style="margin: 0">订单预览</h2>
          </div>
          <div class="receipt-divider"/>
          <div>
            <div class="item-row">
              <span>基础配送费</span>
              <span>¥{{ basePrice.toFixed(2) }}</span>
            </div>
            <div class="item-row">
              <span>规格附加费</span>
              <span>¥{{ sizePrice.toFixed(2) }}</span>
            </div>
          </div>
          <div class="receipt-divider"/>

          <div class="total-row">
            <span>预估总价</span>
            <span class="total-price">
              ¥{{ totalPrice }}
            </span>
          </div>

          <div>
            <el-button type="primary"
                       size="large"
                       class="pay-btn"
                       @click="submitOrder"
                       :loading="loading"
            >立即支付
            </el-button>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {Box, Location, LocationInformation} from "@element-plus/icons-vue";
import {listStation} from "@/api/take/station.js";
import {listSize} from "@/api/take/size.js";
import {listBuilding} from "@/api/take/building.js";
import {addOrder} from "@/api/take/order.js";
import {ElMessage} from "element-plus";

import {useRoute, useRouter} from "vue-router";

//加载状态
const loading = ref(false)

//基础配送费
const basePrice = 2.0

//计算规格附加费
const sizePrice = computed(() => {
  const option = sizeList.value.find(s => s.sizeId === form.value.sizeId)
  return option ? option.price : 0
})

//计算总价
const totalPrice = computed(() => {
  let total = basePrice + sizePrice.value
  return total.toFixed(2)
})

//表单实例
const formRef = ref()

//表单参数
const form = ref({
  code: null,
  sizeId: null,
  buildingId: null,
  stationId: null,
  room: null,
  totalPrice: null,
  remark: null
})

//路由实例
const router = useRouter()

//表单校验
const rules = ref({
  code: [
    {required: true, message: "取件码不能为空", trigger: "blur"}
  ],
  sizeId: [
    {required: true, message: "包裹规格不能为空", trigger: "blur"}
  ],
  buildingId: [
    {required: true, message: "宿舍楼不能为空", trigger: "blur"}
  ],
  stationId: [
    {required: true, message: "快递站点不能为空", trigger: "blur"}
  ],
  room: [
    {required: true, message: "寝室号/联系人不能为空", trigger: "blur"}
  ],
  totalPrice: [
    {required: true, message: "总价不能为空", trigger: "blur"}
  ],
})

//提交订单
const submitOrder = () => {
  form.value.totalPrice = totalPrice.value

  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true

      //调用api提交订单
      addOrder(form.value).then(res => {
        loading.value = false
        ElMessage.success('订单支付成功! 配送员开始接单')
        //跳转到我的订单页面
        router.push('/user/orders')

      }).catch(() => {
        loading.value = false
      })
    }
  })
}

//站点列表
const stationList = ref([])
//规格列表
const sizeList = ref([])
//宿舍楼列表
const buildingList = ref([])

const route = useRoute()

onMounted(() => {
  //查询所有快递站点
  listStation({pageNum: 1, pageSize: 100}).then(res => {
    stationList.value = res.rows
  })
  //查询所有规格列表
  listSize({pageNum: 1, pageSize: 100}).then(res => {
    sizeList.value = res.rows
  })
  //查询所有宿舍楼
  listBuilding({pageNum: 1, pageSize: 100}).then(res => {
    buildingList.value = res.rows
  })

  //组件挂载时，如果有携带参数，就赋值给表单
  const { stationId,code,sizeId,buildingId,room} = route.query
  if (stationId) form.value.stationId = stationId
  if (code) form.value.code = code
  if (sizeId) form.value.sizeId = sizeId
  if (buildingId) form.value.buildingId = buildingId
  if (room) form.value.room = room
})
</script>

<style scoped>
/* 主容器样式 */
.takeout-container {
  min-height: calc(100vh - 70px); /* 视口高度减去头部高度 */
  padding: 30px 0; /* 上下内边距 */
  display: flex; /* 启用 Flexbox 布局 */
  justify-content: center; /* 水平居中 */
}

/* 主布局容器 */
.main-layout {
  width: 1100px; /* 固定宽度 */
  display: flex; /* 启用 Flexbox 布局 */
  gap: 30px; /* 子元素之间的间距 */
  align-items: flex-start; /* 子元素顶部对齐，使右侧 sticky 定位生效 */
}

/* 左侧表单区域 */
.form-section {
  flex: 1; /* 占据剩余空间 */
}

/* 右侧汇总区域 */
.summary-section {
  width: 340px; /* 固定宽度 */
  position: sticky; /* 粘性定位 */
  top: 90px; /* 距离顶部的距离 (header高度 + 间距) */
}

/* 表单卡片通用样式 */
.form-card {
  background: #fff; /* 白色背景 */
  border-radius: 16px; /* 圆角 */
  padding: 25px; /* 内边距 */
  margin-bottom: 20px; /* 底部外边距 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03); /* 阴影效果 */
  border: 1px solid #eee; /* 边框 */
}

/* 卡片标题样式 */
.card-title {
  font-size: 16px; /* 字体大小 */
  font-weight: 600; /* 字体粗细 */
  color: #333; /* 字体颜色 */
  display: flex; /* Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  gap: 8px; /* 图标和文字间距 */
  margin-bottom: 20px; /* 底部外边距 */
  padding-bottom: 10px; /* 底部内边距 */
  border-bottom: 1px solid #f5f5f5; /* 底部边框 */
}

/* 规格选择器容器 */
.size-selector {
  display: flex; /* 1. 改为 Flex 布局，水平排列子元素 */
  width: 100%; /* 2. 确保容器宽度为 100% */
  gap: 15px; /* 子元素之间的间距 */
}

/* 单个规格卡片样式 */
.size-card {
  flex: 1; /* 每个卡片等分可用空间 */
  border: 2px solid #eee; /* 边框 */
  border-radius: 12px; /* 圆角 */
  padding: 15px; /* 内边距 */
  cursor: pointer; /* 鼠标手型 */
  position: relative; /* 相对定位，为子元素绝对定位提供参考 */
  transition: all 0.3s ease; /* 过渡动画效果 */
  background: #fff; /* 背景色 */
  display: flex; /* Flexbox 布局 */
  flex-direction: column; /* 垂直方向排列 */
  align-items: center; /* 水平居中 */
  text-align: center; /* 文本居中 */
  min-width: 0; /* 防止内容溢出 */
}

/* 规格卡片悬停效果 */
.size-card:hover {
  border-color: #a0dca0; /* 悬停时边框颜色 */
  background: #fafffa; /* 悬停时背景色 */
}

/* 规格卡片选中状态 */
.size-card.active {
  border-color: #3AAE6E; /* 选中时边框颜色 */
  background: #f0f9f4; /* 选中时背景色 */
  box-shadow: 0 4px 12px rgba(58, 174, 110, 0.15); /* 选中时阴影 */
}

/* 规格图标样式 */
.size-icon {
  font-size: 24px; /* 图标大小 */
  margin-bottom: 8px; /* 底部外边距 */
}

/* 规格名称样式 */
.size-name {
  font-weight: 600; /* 字体粗细 */
  color: #333; /* 字体颜色 */
  font-size: 14px; /* 字体大小 */
}

/* 规格描述样式 */
.size-desc {
  font-size: 11px; /* 字体大小 */
  color: #999; /* 字体颜色 */
  margin-top: 2px; /* 顶部外边距 */
}

/* 规格价格样式 */
.size-price {
  margin-top: 10px; /* 顶部外边距 */
  font-size: 12px; /* 字体大小 */
  color: #3AAE6E; /* 字体颜色 */
  font-weight: bold; /* 字体粗细 */
  background: rgba(58, 174, 110, 0.1); /* 半透明背景色 */
  padding: 2px 8px; /* 内边距 */
  border-radius: 10px; /* 圆角 */
}

/* 选中标记样式 */
.check-mark {
  position: absolute; /* 绝对定位 */
  top: -10px; /* 向上偏移 */
  right: -10px; /* 向右偏移 */
  background: #3AAE6E; /* 背景色 */
  color: #fff; /* 字体颜色 */
  width: 20px; /* 宽度 */
  height: 20px; /* 高度 */
  border-radius: 50%; /* 圆形 */
  display: flex; /* Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  font-size: 12px; /* 字体大小 */
}

/* 右侧票据卡片样式 */
.receipt-card {
  background: #fff; /* 白色背景 */
  border-radius: 12px; /* 圆角 */
  padding: 20px; /* 内边距 */
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06); /* 阴影效果 */
  position: relative; /* 相对定位 */
  border-top: 4px solid #3AAE6E; /* 顶部边框 */
}

/* 票据头部样式 */
.receipt-header {
  display: flex; /* Flexbox 布局 */
  justify-content: space-between; /* 两端对齐 */
  align-items: flex-end; /* 底部对齐 */
  margin-bottom: 15px; /* 底部外边距 */
  color: #999; /* 字体颜色 */
}

/* 票据分隔线样式 */
.receipt-divider {
  height: 1px; /* 高度 */
  background: #eee; /* 背景色 */
  margin: 15px 0; /* 上下外边距 */
  border-bottom: 1px dashed #ddd; /* 虚线边框 */
}

/* 费用项目行样式 */
.item-row {
  display: flex; /* Flexbox 布局 */
  justify-content: space-between; /* 两端对齐 */
  margin-bottom: 12px; /* 底部外边距 */
  font-size: 14px; /* 字体大小 */
  color: #555; /* 字体颜色 */
}

/* 总价行样式 */
.total-row {
  display: flex; /* Flexbox 布局 */
  justify-content: space-between; /* 两端对齐 */
  align-items: center; /* 垂直居中 */
  margin-top: 15px; /* 顶部外边距 */
  margin-bottom: 20px; /* 底部外边距 */
  font-weight: 600; /* 字体粗细 */
}

/* 总价数字样式 */
.total-price {
  color: #3AAE6E; /* 字体颜色 */
  font-size: 28px; /* 字体大小 */
  font-weight: 800; /* 字体粗细 */
}

/* 支付按钮样式 */
.pay-btn {
  width: 100%; /* 宽度100% */
  border-radius: 8px; /* 圆角 */
  font-weight: 600; /* 字体粗细 */
  background-color: #3AAE6E; /* 背景色 */
  border-color: #3AAE6E; /* 边框颜色 */
}

/* 支付按钮悬停效果 */
.pay-btn:hover {
  background-color: #329a60; /* 悬停背景色 */
  border-color: #329a60; /* 悬停边框色 */
}

/* 安全提示样式 */
.security-tip {
  margin-top: 20px; /* 顶部外边距 */
  text-align: center; /* 文本居中 */
  color: #99a9bf; /* 字体颜色 */
  font-size: 12px; /* 字体大小 */
  display: flex; /* Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  gap: 5px; /* 图标和文字间距 */
}
</style>
