<template>
  <div class="recharge-container">
    <div class="main-content">
      <!-- 显示余额 -->
      <div class="balance-card">
        <div class="balance-info">
          <span class="label">当前余额</span>
          <div class="amount-row">
            <span class="symbol">¥</span>
            <span class="num">{{ balance.toFixed(2) }}</span>
          </div>
        </div>
        <div class="card-decoration">
          <el-icon>
            <Wallet/>
          </el-icon>
        </div>
      </div>

      <!-- 充值面板 -->
      <div class="action-panel">
        <div class="section-title">充值金额</div>
        <div class="amount-grid">
          <div v-for="amount in presetAmounts"
               :key="amount"
               class="amount-item"
               :class="{ active: selectedAmount === amount }"
               @click="selectPreset(amount)"
          >
            <span class="val">{{ amount }}元</span>
          </div>

          <!-- 自定义金额 -->
          <div class="amount-item custom-input" :class="{ active: isCustomAmount }">
            <el-input-number v-model="customVal"
                             :min="1"
                             :controls="false"
                             placeholder="自定义"
                             @focus="handleCustomFocus"
                             class="input-box"
            >
              <span class="suffix" v-if="!customVal">元</span>
            </el-input-number>
          </div>
        </div>

        <!-- 支付方式 -->
        <div class="section-title mt-30">支付方式</div>
        <div class="payment-methods">
          <div v-for="method in paymentMethodsList"
               :key="method.value"
               class="pay-item"
               :class="{ active: paymentMethod === method.value }"
               @click="paymentMethod = method.value"
          >
            <div class="icon-box">
              <SvgIcon style="font-size: 35px" icon-class="微信" v-if="method.value === 'wechat'"/>
              <SvgIcon style="font-size: 35px" icon-class="支付宝" v-if="method.value === 'alipay'"/>
              <SvgIcon style="font-size: 35px" icon-class="银行卡" v-if="method.value === 'card'"/>
            </div>
            <div class="pay-info">
              <div class="name">{{ method.name }}</div>
              <div class="desc">{{ method.desc }}</div>
            </div>
            <div class="radio-check">
              <el-icon v-if="paymentMethod === method.value"><Select/></el-icon>
            </div>
          </div>
        </div>

        <!-- 提交区域 -->
        <div class="submit-area">
          <div class="total-text">
            实付金额: <span class="price">¥{{ finalPayAmount }}</span>
          </div>
          <el-button type="primary"
                     size="large"
                     class="pay-btn"
                     :loading="loading"
                     @click="handleRecharge"
          >
            立即充值
          </el-button>
        </div>

      </div>


    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { selectMyBalance, recharge } from "@/api/system/user.js";
import { Select, Wallet } from "@element-plus/icons-vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
import { ElMessage } from "element-plus";

// 支付方式列表配置
const paymentMethodsList = ref([
  {
    value: 'wechat',
    name: '微信支付',
    desc: '推荐使用，安全快捷'
  },
  {
    value: 'alipay',
    name: '支付宝',
    desc: '数亿用户的选择'
  },
  {
    value: 'card',
    name: '银行卡',
    desc: '支持储蓄卡/信用卡'
  }
])

//加载状态
const loading = ref(false)

//金额选项
const presetAmounts = [10, 30, 50, 100, 200]
//自定义金额
const customVal = ref(null)
//当前选中的预设金额
const selectedAmount = ref(50)
//当前是否正在使用自定义金额
const isCustomAmount = ref(false)
//当前选中的支付方式
const paymentMethod = ref('wechat')

//账户余额
const balance = ref(0)

//计算最终支付金额
const finalPayAmount = computed(() => {
  if (isCustomAmount.value) {
    return customVal.value ? Number(customVal.value).toFixed(2) : '0.00'
  }
  return selectedAmount.value.toFixed(2)
})

//选择预设金额
const selectPreset = (val) => {
  isCustomAmount.value = false
  selectedAmount.value = val
  customVal.value = null
}

//选择自定义金额输入
const handleCustomFocus = () => {
  isCustomAmount.value = true
  selectedAmount.value = null
}

//查询账户余额
const getMyBalance = () => {
  selectMyBalance().then(res => {
    balance.value = res.data
  })
}

//充值按钮
const handleRecharge = () => {
  const amount = parseFloat(finalPayAmount.value)
  if (amount <= 0) {
    ElMessage.warning('请输入有效的充值金额')
    return
  }
  loading.value = true
  //调用充值api
  recharge(amount).then(res => {
    getMyBalance()
    loading.value = false
  })
}

onMounted(() => {
  getMyBalance()
})
</script>

<style scoped>
/*
 * 充值页面容器
 * 使用flex布局居中，设置最小高度确保页面占满视口
 */
.recharge-container {
  min-height: calc(100vh - 70px); /* 减去可能的头部高度 */
  padding: 30px 0;
  display: flex;
  justify-content: center;
}

/*
 * 主内容区域
 * 固定宽度，保持内容聚焦
 */
.main-content {
  width: 800px;
}

/*
 * 余额卡片样式
 * 使用渐变色背景，圆角阴影提升视觉层次
 */
.balance-card {
  background: linear-gradient(135deg, #3AAE6E 0%, #42ca7f 100%); /* 135度绿色渐变 */
  border-radius: 16px; /* 大圆角 */
  padding: 30px 40px; /* 内边距 */
  color: #fff; /* 白色文字 */
  position: relative; /* 相对定位，用于装饰元素定位 */
  overflow: hidden; /* 隐藏溢出的装饰元素 */
  box-shadow: 0 10px 20px rgba(58, 174, 110, 0.2); /* 绿色阴影 */
  margin-bottom: 25px; /* 与下方内容间距 */
}

/* 余额标签样式 */
.balance-info .label {
  font-size: 14px;
  opacity: 0.9; /* 轻微透明 */
}

/* 金额显示行布局 */
.amount-row {
  margin-top: 10px;
  display: flex;
  align-items: baseline; /* 基线对齐 */
}

/* 货币符号样式 */
.amount-row .symbol {
  font-size: 20px;
  margin-right: 4px;
}

/* 金额数字样式 */
.amount-row .num {
  font-size: 42px;
  font-weight: 700; /* 加粗 */
  line-height: 1; /* 单倍行距 */
}

/*
 * 卡片装饰元素
 * 使用大号半透明图标作为背景装饰
 */
.card-decoration {
  position: absolute; /* 绝对定位 */
  right: -20px; /* 右侧偏移 */
  bottom: -40px; /* 底部偏移 */
  font-size: 180px; /* 超大字号 */
  opacity: 0.15; /* 低透明度 */
  transform: rotate(-15deg); /* 旋转角度 */
  pointer-events: none; /* 禁止交互 */
}

/*
 * 操作面板
 * 白色背景卡片，轻微阴影
 */
.action-panel {
  background: #fff;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04); /* 浅色阴影 */
}

/*
 * 区域标题样式
 * 左侧绿色竖条作为视觉分隔
 */
.section-title {
  font-size: 16px;
  font-weight: 600; /* 中等加粗 */
  color: #333;
  margin-bottom: 15px;
  padding-left: 10px; /* 左侧内边距 */
  border-left: 4px solid #3AAE6E; /* 左侧绿色竖条 */
  line-height: 1; /* 单倍行距 */
}

/* 上方间距工具类 */
.mt-30 {
  margin-top: 30px;
}

/*
 * 金额网格布局
 * 三列网格，等宽分布
 */
.amount-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 三列等宽 */
  gap: 15px; /* 网格间距 */
}

/*
 * 金额选项基础样式
 */
.amount-item {
  border: 1px solid #e0e0e0; /* 浅灰色边框 */
  border-radius: 8px;
  height: 70px; /* 固定高度 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer; /* 鼠标手形 */
  position: relative;
  transition: all 0.2s; /* 过渡动画 */
  background: #fff;
}

/* 金额选项悬停效果 */
.amount-item:hover {
  border-color: #3AAE6E; /* 绿色边框 */
  color: #3AAE6E; /* 绿色文字 */
}

/* 金额选项选中状态 */
.amount-item.active {
  background-color: #f0f9f4; /* 浅绿色背景 */
  border-color: #3AAE6E; /* 绿色边框 */
  color: #3AAE6E; /* 绿色文字 */
  font-weight: 600; /* 加粗 */
  box-shadow: 0 2px 8px rgba(58, 174, 110, 0.15); /* 绿色阴影 */
}

/* 金额值文字样式 */
.amount-item .val {
  font-size: 18px;
}

/* 折扣标签样式 */
.amount-item .discount {
  font-size: 11px;
  color: #ff6b6b; /* 红色文字 */
  position: absolute; /* 绝对定位 */
  top: 5px;
  right: 5px;
  background: rgba(255, 107, 107, 0.1); /* 半透红色背景 */
  padding: 1px 4px;
  border-radius: 4px;
}

/*
 * 自定义输入框特殊样式
 */
.custom-input {
  padding: 0; /* 移除内边距 */
  overflow: hidden; /* 隐藏溢出内容 */
}

/* 输入框容器 */
.custom-input .input-box {
  width: 100%;
}

/*
 * 深度选择器修改 Element Plus 组件样式
 * ::v-deep 或 :deep() 用于穿透scoped样式
 */
:deep(.el-input-number .el-input__wrapper) {
  box-shadow: none !important; /* 移除默认阴影 */
  background: transparent !important; /* 透明背景 */
  padding: 0; /* 移除内边距 */
}

/* 输入框内部样式 */
:deep(.el-input__inner) {
  text-align: center; /* 文字居中 */
  font-size: 16px;
  height: 68px; /* 高度与金额选项一致 */
}

/* 自定义输入框选中状态 */
.custom-input.active :deep(.el-input__inner) {
  color: #3AAE6E; /* 绿色文字 */
  font-weight: 600; /* 加粗 */
}

/*
 * 支付方式列表布局
 */
.payment-methods {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  gap: 12px; /* 选项间距 */
}

/*
 * 支付选项基础样式
 */
.pay-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border: 1px solid #eee; /* 浅灰色边框 */
  border-radius: 10px;
  cursor: pointer; /* 鼠标手形 */
  transition: all 0.2s; /* 过渡动画 */
}

/* 支付选项悬停效果 */
.pay-item:hover {
  background: #fdfdfd; /* 浅灰色背景 */
}

/* 支付选项选中状态 */
.pay-item.active {
  border-color: #3AAE6E; /* 绿色边框 */
  background: #f0f9f4; /* 浅绿色背景 */
}

/*
 * 支付方式图标容器
 */
.icon-box {
  width: 40px; /* 固定宽度 */
  height: 40px; /* 固定高度 */
  border-radius: 50%; /* 圆形 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px; /* 右侧间距 */
}

/* 支付方式信息区域 */
.pay-info {
  flex: 1; /* 占据剩余空间 */
}

/* 支付方式名称 */
.pay-info .name {
  font-size: 15px;
  font-weight: 500; /* 中等加粗 */
  color: #333;
}

/* 支付方式描述 */
.pay-info .desc {
  font-size: 12px;
  color: #999; /* 灰色文字 */
  margin-top: 2px; /* 上方间距 */
}

/*
 * 单选按钮样式
 */
.radio-check {
  width: 20px; /* 固定宽度 */
  height: 20px; /* 固定高度 */
  border: 2px solid #ddd; /* 灰色边框 */
  border-radius: 50%; /* 圆形 */
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff; /* 白色图标 */
  font-size: 12px;
  transition: all 0.2s; /* 过渡动画 */
}

/* 单选按钮选中状态 */
.pay-item.active .radio-check {
  background: #3AAE6E; /* 绿色背景 */
  border-color: #3AAE6E; /* 绿色边框 */
}

/*
 * 提交区域布局
 */
.submit-area {
  margin-top: 40px; /* 上方间距 */
  display: flex;
  align-items: center;
  justify-content: space-between; /* 两端对齐 */
}

/* 总计文字样式 */
.total-text {
  font-size: 14px;
  color: #666; /* 深灰色 */
}

/* 价格数字样式 */
.total-text .price {
  font-size: 28px; /* 大字号 */
  color: #333; /* 黑色 */
  font-weight: 700; /* 加粗 */
  margin-left: 5px; /* 左侧间距 */
}

/*
 * 支付按钮样式
 */
.pay-btn {
  width: 200px; /* 固定宽度 */
  border-radius: 8px; /* 圆角 */
  font-weight: 600; /* 加粗 */
  background-color: #3AAE6E; /* 绿色背景 */
  border-color: #3AAE6E; /* 绿色边框 */
}

/* 支付按钮悬停效果 */
.pay-btn:hover {
  background-color: #329a60; /* 深绿色背景 */
  border-color: #329a60; /* 深绿色边框 */
}
</style>
