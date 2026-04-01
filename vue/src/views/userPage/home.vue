<template>
  <div>
    <header class="hero-section">
      <div class="hero-bg-shape"></div>
      <div class="hero-bg-shape"></div>

      <div class="hero-content">
        <div class="hero-text">
          <div class="tag-pill">🚚 专注校园最后500米</div>
          <h1 class="main-title">
            你的快递 <br>
            <span class="typing-effect">我们来搬</span>
          </h1>
          <p class="sub-title">不仅是送货，更懂你的急切。支持菜鸟驿站、丰巢、顺丰直营点等全校快递点代取。</p>

          <div class="stats-row">
            <div class="stat-item">
              <div class="stat-num">¥2.0</div>
              <div class="stat-label">起步价</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">98%</div>
              <div class="stat-label">半小时达</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">0损</div>
              <div class="stat-label">全额赔付</div>
            </div>
          </div>
        </div>

        <div class="action-card glass-panel">
          <div class="card-header-text">
            <span>📦 极速下单</span>
          </div>

          <div class="flow-inputs">
            <el-select
                v-model="form.stationId"
                placeholder="包裹在哪里？(选择快递点)"
                size="large"
                class="station-select"
            >
              <template #prefix>
                <el-icon>
                  <Location/>
                </el-icon>
              </template>
              <el-option v-for="station in stationList"
                         :key="station.stationId"
                         :label="station.name"
                         :value="station.stationId"
              />
            </el-select>

            <el-input
                v-model="form.code"
                placeholder="取件码 (例如：3-2056)"
                resize="none"
            />

            <div class="row-inputs">
              <el-select v-model="form.buildingId" placeholder="送到哪栋楼" style="flex: 1">
                <el-option v-for="building in buildingList"
                           :key="building.buildingId"
                           :label="building.name"
                           :value="building.buildingId"
                />
              </el-select>
              <el-input v-model="form.room" placeholder="寝室号" style="width: 100px"/>
            </div>

            <div class="size-selector-wrapper">
              <div class="label-text">包裹规格</div>
              <el-radio-group v-model="form.sizeId" size="small" fill="#3db071">
                <el-radio-button v-for="size in sizeList"
                                 :key="size.sizeId"
                                 :label="size.sizeId"
                >{{ size.name }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <el-button type="primary" size="large" class="hero-btn" @click="openFullOrderModal">
            立即下单
            <el-icon class="ml-2">
              <ArrowRight/>
            </el-icon>
          </el-button>
        </div>
      </div>
    </header>

    <main class="main-body">

      <div class="data-dashboard">
        <div class="rank-column">
          <div class="panel-header">
            <h3>📢 校园公告</h3>
          </div>
          <div class="station-list">
            <div class="station-item" v-for="(notice, idx) in noticeList" :key="notice.title">
              <div class="idx" :class="'idx-'+(idx+1)">{{ idx + 1 }}</div>
              <div class="info">
                <div class="name clickable-name" @click="openDetail(notice)">{{ notice.title }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="process-column">
          <div class="panel-header">
            <h3>🛡️ 平台保障</h3>
          </div>
          <div class="guarantee-grid">
            <div class="g-item">
              <el-icon class="g-icon">
                <Key/>
              </el-icon>
              <h4>隐私保护</h4>
              <p>取件码阅后即焚</p>
            </div>
            <div class="g-item">
              <el-icon class="g-icon">
                <Umbrella/>
              </el-icon>
              <h4>丢损必赔</h4>
              <p>最高赔付 ¥2000</p>
            </div>
            <div class="g-item">
              <el-icon class="g-icon">
                <AlarmClock/>
              </el-icon>
              <h4>超时免单</h4>
              <p>超45分钟全额退</p>
            </div>
            <div class="g-item">
              <el-icon class="g-icon">
                <UserFilled/>
              </el-icon>
              <h4>实名认证</h4>
              <p>本校学生骑手</p>
            </div>
          </div>

        </div>
      </div>
    </main>

    <vxe-modal
        v-model="open"
        :title="currentDetail.title"
        width="600px"
        show-maximize showFooter resize
    >
      <div class="dialog-meta">
        <span>发布时间：{{ currentDetail.createTime }}</span>
      </div>
      <div class="dialog-content">
        <p v-html="currentDetail.content"></p>
      </div>
      <template #footer>
        <span class="dialog-footer">
            <el-button type="primary" @click="open = false">关 闭</el-button>
        </span>
      </template>
    </vxe-modal>

  </div>
</template>

<script setup>
import {onMounted, onUnmounted, reactive, ref} from 'vue'
import {AlarmClock, ArrowRight, Key, Location, Umbrella, UserFilled} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import {listStation} from "@/api/take/station.js";
import {listSize} from "@/api/take/size.js";
import {listBuilding} from "@/api/take/building.js";
import {listNotice} from "@/api/take/notice.js";

// 快速下单表单
const form = reactive({
  stationId: null, // 快递点
  buildingId: null,
  room: null,
  sizeId: null,
  code: null,
})

const noticeList = ref([])

const router = useRouter()

const open = ref(false)
const currentDetail = ref({})

const openDetail = (item) => {
  currentDetail.value = item
  open.value = true
}

const openFullOrderModal = () => {
  if (!form.stationId) {
    ElMessage.warning('请选择包裹所在的快递点')
    return
  }
  if (!form.code) {
    ElMessage.warning('请粘贴取件码')
    return
  }

  // 跳转到takeout页面并传递表单数据
  router.push({
    path: '/user/takeout',
    query: {
      stationId: form.stationId,
      code: form.code,
      sizeId: form.sizeId,
      buildingId: form.buildingId,
      room: form.room,
    }
  })
}

//站点列表数据
const stationList = ref([])

//包裹规格列表数据
const sizeList = ref([])

//宿舍楼列表数据
const buildingList = ref([])

onMounted(() => {
  //查询所有快递站点
  listStation({pageNum: 1, pageSize: 100}).then(res => {
    stationList.value = res.rows
  })

  //查询所有包裹规格
  listSize({pageNum: 1, pageSize: 100}).then(res => {
    sizeList.value = res.rows
  })

  //查询所有宿舍楼
  listBuilding({pageNum: 1, pageSize: 100}).then(res => {
    buildingList.value = res.rows
  })

  listNotice({pageNum: 1, pageSize: 5}).then(res => {
    noticeList.value = res.rows
  })
})
</script>

<style scoped>
/* 英雄区域（页面顶部区域）样式 */
.hero-section {
  /* 相对定位，作为子元素的定位参考 */
  position: relative;
  /* 顶部内边距120px，底部内边距60px */
  padding-top: 120px;
  padding-bottom: 60px;
  /* 隐藏溢出内容 */
  overflow: hidden;
}

/* 背景装饰形状 */
.hero-bg-shape {
  /* 绝对定位，相对于父容器 */
  position: absolute;
  /* 50%圆角，使其成为圆形 */
  border-radius: 50%;
  /* 高斯模糊效果，60px模糊半径 */
  filter: blur(60px);
}

/* 英雄区域内容容器 */
.hero-content {
  /* 最大宽度1100px，限制在大屏幕上不会过宽 */
  max-width: 1100px;
  /* 水平居中 */
  margin: 0 auto;
  /* 左右内边距20px，在小屏幕上留出边距 */
  padding: 0 20px;
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 堆叠层级为1，确保在背景上方 */
  position: relative;
  z-index: 1;
}

/* 标签样式（"🚚 专注校园最后500米"） */
.tag-pill {
  /* 行内块元素，可以设置宽高但不会换行 */
  display: inline-block;
  /* 内边距：上下4px，左右12px */
  padding: 4px 12px;
  /* 浅蓝色背景 */
  background: #e7f5ff;
  /* 绿色文字 */
  color: #3db071;
  /* 20px圆角，形成胶囊形状 */
  border-radius: 20px;
  /* 字体大小12px */
  font-size: 12px;
  /* 半粗字体 */
  font-weight: 600;
  /* 底部外边距16px，与下方元素分隔 */
  margin-bottom: 16px;
}

/* 主标题样式 */
.main-title {
  /* 字体大小48px */
  font-size: 48px;
  /* 最粗字体 */
  font-weight: 900;
  /* 行高1.1倍，紧凑排版 */
  line-height: 1.1;
  /* 底部外边距20px */
  margin-bottom: 20px;
  /* 深灰色文字 */
  color: #212529;
}

/* 打字机效果文本 */
.typing-effect {
  /* 绿色文字 */
  color: #3db071;
}

/* 副标题样式 */
.sub-title {
  /* 字体大小16px */
  font-size: 16px;
  /* 中灰色文字 */
  color: #666;
  /* 底部外边距30px */
  margin-bottom: 30px;
  /* 最大宽度500px，防止文本过长 */
  max-width: 500px;
  /* 行高1.6倍，增加可读性 */
  line-height: 1.6;
}

/* 统计数据行 */
.stats-row {
  /* 使用flex布局 */
  display: flex;
  /* 子元素之间的间距为40px */
  gap: 40px;
}

/* 统计数据数字 */
.stat-num {
  /* 字体大小24px */
  font-size: 24px;
  /* 粗体 */
  font-weight: 700;
  /* 深灰色文字 */
  color: #333;
}

/* 统计数据标签 */
.stat-label {
  /* 字体大小12px */
  font-size: 12px;
  /* 浅灰色文字 */
  color: #888;
}

/* 玻璃效果面板（右侧下单卡片） */
.glass-panel {
  /* 固定宽度380px */
  width: 380px;
  /* 半透明白色背景，90%不透明度 */
  background: rgba(255, 255, 255, 0.9);
  /* 背景模糊效果，12px模糊半径 */
  backdrop-filter: blur(12px);
  /* 白色边框 */
  border: 1px solid #fff;
  /* 20px圆角 */
  border-radius: 20px;
  /* 蓝色阴影：x偏移0，y偏移12px，模糊30px，透明度0.15的蓝色 */
  box-shadow: 0 12px 30px rgba(76, 110, 245, 0.15);
  /* 内边距24px */
  padding: 24px;
}

/* 卡片头部文本 */
.card-header-text {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 底部外边距20px */
  margin-bottom: 20px;
  /* 粗体 */
  font-weight: 700;
  /* 字体大小18px */
  font-size: 18px;
}

/* 流程输入框容器 */
.flow-inputs {
  /* 使用flex布局 */
  display: flex;
  /* 主轴方向为垂直（列） */
  flex-direction: column;
  /* 子元素之间的间距为12px */
  gap: 12px;
  /* 底部外边距20px */
  margin-bottom: 20px;
}

/* 行输入框组（宿舍楼选择器和寝室号输入框） */
.row-inputs {
  /* 使用flex布局 */
  display: flex;
  /* 子元素之间的间距为10px */
  gap: 10px;
}

/* 包裹规格选择器容器 */
.size-selector-wrapper {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 浅灰色背景 */
  background: #f1f3f5;
  /* 内边距：上下8px，左右12px */
  padding: 8px 12px;
  /* 8px圆角 */
  border-radius: 8px;
}

/* 标签文本 */
.label-text {
  /* 字体大小12px */
  font-size: 12px;
  /* 中灰色文字 */
  color: #666;
  /* 半粗字体 */
  font-weight: 600;
}

/* 英雄区域按钮样式 */
.hero-btn {
  /* 宽度100%，占满父容器 */
  width: 100%;
  /* 绿色背景 */
  background-color: #3db071;
  /* 无边框 */
  border: none;
  /* 10px圆角 */
  border-radius: 10px;
}

/* 英雄按钮悬停效果 */
.hero-btn:hover {
  /* 向上移动1px，产生上浮效果 */
  transform: translateY(-1px);
}

/* 主体区域 */
.main-body {
  /* 最大宽度1100px，限制在大屏幕上不会过宽 */
  max-width: 1100px;
  /* 水平居中 */
  margin: 0 auto;
  /* 内边距：上下40px，左右20px */
  padding: 40px 20px;
}

/* 数据仪表板（底部公告和保障区域） */
.data-dashboard {
  /* 使用grid网格布局 */
  display: grid;
  /* 两列，每列等宽（1fr表示一个分数单位） */
  grid-template-columns: 1fr 1fr;
  /* 列与列之间的间距为30px */
  gap: 30px;
}

/* 左侧列（公告区域）和右侧列（保障区域）公共样式 */
.rank-column, .process-column {
  /* 白色背景 */
  background: #fff;
  /* 16px圆角 */
  border-radius: 16px;
  /* 内边距24px */
  padding: 24px;
  /* 浅灰色边框 */
  border: 1px solid #f1f3f5;
}

/* 面板头部（标题区域） */
.panel-header {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 底部外边距20px */
  margin-bottom: 20px;
}

/* 面板标题 */
.panel-header h3 {
  /* 字体大小16px */
  font-size: 16px;
  /* 移除默认外边距 */
  margin: 0;
}

/* 站点列表（公告列表）容器 */
.station-list {
  /* 使用flex布局 */
  display: flex;
  /* 主轴方向为垂直（列） */
  flex-direction: column;
  /* 子元素之间的间距为15px */
  gap: 15px;
}

/* 站点项（公告项）样式 */
.station-item {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 底部内边距12px */
  padding-bottom: 12px;
  /* 底部浅灰色实线边框 */
  border-bottom: 1px solid #f8f9fa;
}

/* 最后一个站点项的特殊样式 */
.station-item:last-child {
  /* 移除边框 */
  border: none;
  /* 移除内边距 */
  padding: 0;
}

/* 序号样式 */
.idx {
  /* 固定宽度20px */
  width: 20px;
  /* 最粗字体 */
  font-weight: 800;
  /* 斜体 */
  font-style: italic;
  /* 浅灰色文字 */
  color: #ced4da;
}

/* 信息区域（公告标题等） */
.info {
  /* 占据剩余空间的比例为1 */
  flex: 1;
  /* 左侧外边距10px，与序号分隔 */
  margin-left: 10px;
}

/* 名称（公告标题）样式 */
.name {
  /* 字体大小14px */
  font-size: 14px;
  /* 半粗字体 */
  font-weight: 600;
  /* 中灰色文字 */
  color: #495057;
}

/* 可点击的名称样式 */
.clickable-name {
  /* 鼠标指针变为手形，表示可点击 */
  cursor: pointer;
  /* 文字颜色变化在0.2秒内完成 */
  transition: color 0.2s;
}

/* 可点击名称悬停效果 */
.clickable-name:hover {
  /* 悬停时变为绿色 */
  color: #3db071;
}

/* 保障网格容器 */
.guarantee-grid {
  /* 使用grid网格布局 */
  display: grid;
  /* 两列，每列等宽 */
  grid-template-columns: 1fr 1fr;
  /* 行和列之间的间距为15px */
  gap: 15px;
  /* 底部外边距20px */
  margin-bottom: 20px;
}

/* 保障项样式 */
.g-item {
  /* 文本居中对齐 */
  text-align: center;
  /* 浅灰色背景 */
  background: #f8f9fa;
  /* 内边距15px */
  padding: 15px;
  /* 12px圆角 */
  border-radius: 12px;
}

/* 保障图标样式 */
.g-icon {
  /* 字体大小24px */
  font-size: 24px;
  /* 绿色图标 */
  color: #3db071;
  /* 底部外边距8px，与文字分隔 */
  margin-bottom: 8px;
}

/* 保障项标题 */
.g-item h4 {
  /* 移除默认外边距 */
  margin: 0;
  /* 字体大小13px */
  font-size: 13px;
  /* 深灰色文字 */
  color: #333;
}

/* 保障项描述 */
.g-item p {
  /* 上外边距4px，底部外边距0 */
  margin: 4px 0 0;
  /* 字体大小11px */
  font-size: 11px;
  /* 浅灰色文字 */
  color: #999;
}

/* 弹窗元数据区域 */
.dialog-meta {
  /* 文本居中对齐 */
  text-align: center;
  /* 字体大小12px */
  font-size: 12px;
  /* 浅灰色文字 */
  color: #999;
  /* 底部内边距15px */
  padding-bottom: 15px;
  /* 底部虚线边框 */
  border-bottom: 1px dashed #eee;
  /* 底部外边距15px */
  margin-bottom: 15px;
}

/* 弹窗元数据区域内的span元素 */
.dialog-meta span {
  /* 左右外边距10px */
  margin: 0 10px;
}

/* 弹窗内容区域 */
.dialog-content {
  /* 行高1.8倍，增加可读性 */
  line-height: 1.8;
  /* 深灰色文字 */
  color: #444;
  /* 字体大小14px */
  font-size: 14px;
  /* 最小高度100px，确保有足够空间显示内容 */
  min-height: 100px;
}
</style>
