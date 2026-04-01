<template>
  <div class="accept-container">
    <div class="main-content">
      <el-row :gutter="20">
        <!--  订单列表区-->
        <el-col :span="18">
          <div>
            <template v-if="orderList.length >0">
              <div v-for="order in orderList"
                   :key="order.orderId"
                   class="order-card">
                <div class="card-left">
                  <div class="price-badge">
                    <span class="currency">¥</span>
                    <span class="amount">{{order.totalPrice}}</span>
                  </div>
                  <div class="publish-time">{{formatTimeAgo(order.createTime)}}</div>
                </div>
                <div class="card-center">
                  <div class="route-box">
                    <div class="station-row">
                      <el-tag size="small" type="success" effect="dark" class="point-tag">取</el-tag>
                      <span class="addr-text">{{order.station}}</span>
                    </div>
                    <div class="arrow-row">
                      <div class="dashed-line"/>
                      <el-icon><Bicycle/></el-icon>
                    </div>
                    <div class="station-row">
                      <el-tag size="small" type="warning"effect="dark"class="point-tag">送</el-tag>
                      <span class="addr-text">{{order.building}} - {{order.room}}</span>
                    </div>
                  </div>
                  <div class="remark-row" v-if="order.remark">
                    <el-icon><ChatLineSquare/></el-icon>
                    <span>{{order.remark}}</span>
                  </div>
                </div>
                <div class="card-right">

                  <el-button type="primary"
                             class="accept-btn"
                             @click="handleAccept(order)"
                  >
                    接单
                  </el-button>
                </div>
              </div>
            </template>
            <el-empty v-else description="暂无待接订单，休息一下吧~"/>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="rider-dashboard">
            <div class="rider-header">
              <el-avatar :size="50" :src="avatar"/>

              <div class="rider-info">
                <div class="name">{{userName}}</div>
              </div>
            </div>
            <div class="notice-box">
              <div class="notice-title">
                <el-icon>
                  <Warning/>
                </el-icon>
                接单须知
              </div>
              <p>1.接单后请在 45分钟 内送达</p>
              <p>2.取件时请核对取件码和包裹</p>
            </div>
          </div>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script setup>
import {onMounted,ref,computed} from "vue";
import {accept, listOrder} from "../../api/take/order.js";
import {Bicycle, ChatLineSquare, Warning} from "@element-plus/icons-vue";
import useUserStore from "@/store/modules/userStore.js";
import {ElMessage, ElMessageBox} from "element-plus";

// 从用户仓库获取用户信息
const userStore = useUserStore()
const avatar = computed(() => userStore.avatar)
const userName = computed(() => userStore.name)

// 格式化时间
const formatTimeAgo = (timestamp) => {
  let timeValue
  if (typeof timestamp === 'string') {
    const isoString = timestamp.replace(' ', 'T');
    timeValue = new Date(isoString).getTime();
  } else {
    timeValue = timestamp
  }

  const diff = new Date().getTime() - timeValue
  const minutes = Math.floor(diff / (1000*60))

  if(minutes < 1) return '刚刚'
  if(minutes < 60) return `${minutes}分钟前`
  const hours = Math.floor(minutes / 60)
  return `${hours}小时前`
}

//接单
const handleAccept = (order) => {
  ElMessageBox.confirm(`确认接取该订单吗？需从${order.station}送往${order.building}`, '接单确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    accept(order.orderId).then(res =>{
      getList()
      ElMessage.success('接单成功！请尽快前往取件')
    })

  })
}

//查询参数
const query = ref({
  pageNum: 1,
  pageSize: 10,
  sizeId: null,
  status: '待接单',
})

//订单列表
const orderList = ref([])

//查询数据
const getList = () =>{
  listOrder(query.value).then(res =>{
    orderList.value = res.rows
  })
}

onMounted(() =>{
  getList()
})
</script>

<style scoped>
/*
 * 主容器样式
 * 设置最小高度为视口高度减去70px（通常是顶部导航栏高度）
 * 使用弹性布局垂直居中
 */
.accept-container {
  min-height: calc(100vh - 70px); /* 视口高度减去导航栏 */
  padding: 30px 0; /* 上下内边距 */
  display: flex; /* 弹性布局 */
  justify-content: center; /* 水平居中 */
}

/*
 * 主要内容区域
 * 固定宽度1100px，两侧留白
 */
.main-content {
  width: 1100px; /* 固定宽度 */
  padding: 0 20px; /* 左右内边距 */
}

/*
 * 订单卡片样式
 * 卡片化设计，包含悬停效果
 */
.order-card {
  background: #fff; /* 白色背景 */
  border-radius: 12px; /* 圆角边框 */
  margin-bottom: 15px; /* 底部外边距 */
  padding: 20px; /* 内边距 */
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03); /* 轻微阴影 */
  transition: all 0.3s; /* 过渡动画 */
  border: 1px solid transparent; /* 透明边框（为悬停效果预留） */
}

/* 订单卡片悬停效果 */
.order-card:hover {
  transform: translateY(-2px); /* 向上轻微移动 */
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08); /* 加深阴影 */
  border-color: #d1eddb; /* 绿色边框 */
}

/*
 * 卡片左侧区域：价格和时间
 * 固定宽度，右侧虚线分隔
 */
.card-left {
  width: 100px; /* 固定宽度 */
  text-align: center; /* 文字居中 */
  border-right: 1px dashed #eee; /* 右侧虚线边框 */
  margin-right: 20px; /* 右侧外边距 */
  flex-shrink: 0; /* 防止收缩 */
}

/* 价格徽章样式 */
.price-badge {
  color: #f56c6c; /* Element Plus 错误色（红色系） */
  font-weight: 800; /* 加粗字体 */
}

/* 货币符号样式 */
.price-badge .currency {
  font-size: 14px; /* 较小字号 */
}

/* 金额数字样式 */
.price-badge .amount {
  font-size: 24px; /* 大字号突出显示 */
}

/* 发布时间样式 */
.publish-time {
  font-size: 12px; /* 小字号 */
  color: #999; /* 灰色文字 */
  margin-top: 5px; /* 上边距 */
}

/*
 * 卡片中间区域：路线详情
 * 自适应宽度
 */
.card-center {
  flex: 1; /* 占据剩余空间 */
}

/* 路线容器 */
.route-box {
  display: flex; /* 弹性布局 */
  flex-direction: column; /* 垂直排列 */
  gap: 12px; /* 子元素间距 */
}

/* 站点行（取件/送件行） */
.station-row {
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  gap: 10px; /* 子元素间距 */
}

/* 地点标签样式（取/送） */
.point-tag {
  width: 24px; /* 固定宽度 */
  height: 24px; /* 固定高度 */
  padding: 0; /* 无内边距 */
  display: flex; /* 弹性布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  border-radius: 4px; /* 小圆角 */
}

/* 地址文字样式 */
.addr-text {
  font-size: 15px; /* 中等字号 */
  font-weight: 600; /* 半粗体 */
  color: #333; /* 深灰色文字 */
}

/* 包裹尺寸标签样式 */
.size-tag {
  font-size: 12px; /* 小字号 */
  background: #f0f2f5; /* 浅灰色背景 */
  color: #666; /* 中灰色文字 */
  padding: 2px 6px; /* 内边距 */
  border-radius: 4px; /* 圆角 */
}

/*
 * 箭头行：连接取件和送件的虚线
 * 包含自行车图标
 */
.arrow-row {
  padding-left: 11px; /* 左内边距，用于对齐标签中心 */
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  color: #ccc; /* 浅灰色图标 */
  height: 10px; /* 固定高度 */
  margin: -5px 0; /* 负外边距，向上移动使布局更紧凑 */
}

/* 虚线样式 */
.dashed-line {
  height: 100%; /* 继承父元素高度 */
  width: 1px; /* 固定宽度 */
  border-left: 2px dotted #e4e7ed; /* 点状左边框 */
  margin-right: 10px; /* 右侧外边距 */
}

/*
 * 备注行样式
 * 黄色背景突出显示
 */
.remark-row {
  margin-top: 12px; /* 上边距 */
  background: #fff9f0; /* 浅黄色背景 */
  padding: 6px 10px; /* 内边距 */
  border-radius: 4px; /* 圆角 */
  font-size: 12px; /* 小字号 */
  color: #e6a23c; /* Element Plus 警告色（橙色） */
  display: inline-flex; /* 内联弹性布局 */
  align-items: center; /* 垂直居中 */
  gap: 5px; /* 子元素间距 */
  max-width: 100%; /* 最大宽度限制 */
}

/*
 * 卡片右侧区域：接单按钮
 * 固定宽度
 */
.card-right {
  width: 120px; /* 固定宽度 */
  text-align: right; /* 文字右对齐 */
  padding-left: 20px; /* 左内边距 */
}

/*
 * 接单按钮样式
 * 渐变背景，圆角设计
 */
.accept-btn {
  width: 100%; /* 占满容器宽度 */
  height: 40px; /* 固定高度 */
  font-size: 15px; /* 中等字号 */
  font-weight: 600; /* 半粗体 */
  border-radius: 20px; /* 圆形按钮（高度的一半） */
  background: linear-gradient(135deg, #3AAE6E 0%, #42ca7f 100%); /* 绿色渐变 */
  border: none; /* 无边框 */
  box-shadow: 0 4px 10px rgba(58, 174, 110, 0.3); /* 绿色阴影 */
}

/* 按钮悬停效果 */
.accept-btn:hover {
  background: linear-gradient(135deg, #329a60 0%, #3AAE6E 100%); /* 深绿色渐变 */
  transform: scale(1.05); /* 轻微放大 */
}

/*
 * 右侧骑手信息面板
 * 粘性定位，随滚动保持可见
 */
.rider-dashboard {
  background: #fff; /* 白色背景 */
  border-radius: 12px; /* 圆角 */
  padding: 25px 20px; /* 内边距 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03); /* 轻微阴影 */
  position: sticky; /* 粘性定位 */
  top: 90px; /* 距离顶部90px时固定 */
}

/* 骑手头部信息样式 */
.rider-header {
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  gap: 15px; /* 子元素间距 */
  padding-bottom: 20px; /* 底部内边距 */
  border-bottom: 1px solid #f5f5f5; /* 底部边框 */
  margin-bottom: 20px; /* 底部外边距 */
}

/* 骑手姓名样式 */
.rider-info .name {
  font-weight: 600; /* 半粗体 */
  font-size: 16px; /* 较大字号 */
  color: #333; /* 深灰色文字 */
  margin-bottom: 4px; /* 底部外边距 */
}

/*
 * 通知框样式
 * 黄色背景，突出显示重要信息
 */
.notice-box {
  background: #fff8e6; /* 浅黄色背景 */
  border-radius: 8px; /* 圆角 */
  padding: 15px; /* 内边距 */
  color: #d68f12; /* 深黄色文字 */
  font-size: 12px; /* 小字号 */
  line-height: 1.6; /* 行高 */
}

/* 通知标题样式 */
.notice-title {
  font-weight: 700; /* 加粗 */
  margin-bottom: 8px; /* 底部外边距 */
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  gap: 5px; /* 子元素间距 */
}

/* 通知内容段落样式 */
.notice-box p {
  margin: 2px 0; /* 段落间距 */
}
</style>