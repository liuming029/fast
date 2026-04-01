<template>
  <div class="notice-container">
    <div class="main-content">
      <div class="search-bar-card">
        <el-input v-model="query.title"
                  placeholder="搜索公告"
                  clearable
                  style="width: 300px"
                  @clear="getList"

        >
            <template #append>
              <el-button icon="Search" @click="getList"/>
            </template>
        </el-input>
        <span style="font-size: 13px;color:#999;">共找到 {{noticeList.length}} 条公告</span>
      </div>

      <div class="notice-list-card">
        <!-- 🔥 循环必须包裹所有用到 item 的地方 -->
        <div
            v-for="item in noticeList"
            :key="item.noticeId"
            class="notice-item"
            @click="openDetail(item)"
        >
          <div class="notice-left">
            <div class="date-box">
              <!-- 🔥 现在能正常拿到 createTime 了 -->
              <span class="day">{{ getDay(item.createTime) }}</span>
              <span class="month">{{ getMonth(item.createTime) }}月</span>
            </div>
            <div class="info-box">
              <div class="title-row">
                <h3 class="title">{{item.title}}</h3>
              </div>
              <div class="summary">{{getSummary(item.content)}}</div>
              <div class="meta-row">
                <span class="time"><el-icon><Clock/></el-icon> {{item.createTime}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

       <div class="pagination-box">
         <pagination
             v-show="total>0"
             :total="total"
             v-model:page="query.pageNum"
             v-model:limit="query.pageSize"
             @pagination="getList"
         />
       </div>
    </div>
    <vxe-modal :title="currentDetail.title" v-model="open" width="50%" show-maximize showFooter resize>
      <div class="dialog-meta">
        <span>发布时间：{{currentDetail.createTime}}</span>
      </div>
      <div class="dialog-content">
        <p v-html="currentDetail.content"/>
      </div>


    </vxe-modal>
  </div>
</template>

<script setup>
import {onMounted,ref} from "vue";
import {listNotice} from "../../api/take/notice.js";
import {ArrowRight, Clock, Search} from "@element-plus/icons-vue";
import {VxeModal} from "vxe-pc-ui"
//查询参数
const query = ref({
  pageNum: 1,
  pageSize: 10,
  title: null,
})

//对话框是否打开
const open = ref(false)

//当前选中的公告
const currentDetail = ref({})

//打开详情对话框
const openDetail =(notice) =>{
  currentDetail.value =notice
      open.value= true

}

//通知公告列表数据
const noticeList = ref([])

//数据总数
const total = ref(0)

//从日期时间中提取日
const getDay = (dateStr) => {
  return dateStr.substring(8,10)
}
//从日期时间中提取月
const getMonth = (dateStr) => {
  return dateStr.substring(5,7)
}

//从从富文本内容中提取摘要
const getSummary = (content) => {
  const plainText = content.replace(/<[^>]+>/g,"")
      return plainText.length >80 ? plainText.substring(0,80)+'...' :plainText
}

//查询数据
const getList = ()=>{
listNotice(query.value).then(res =>{
noticeList.value = res.rows
  total.value=res.total
})
}
onMounted(() =>{
  getList()
})
</script>



<style scoped>
/* 主容器样式 */
.notice-container {
  /* 最小高度为视口高度减去70px，确保内容占满可视区域 */
  min-height: calc(100vh - 70px);
  /* 上下内边距30px，左右为0 */
  padding: 30px 0;
  /* 使用flex布局，子元素水平居中 */
  display: flex;
  /* 子元素在主轴上居中 */
  justify-content: center;
}

/* 主要内容区域 */
.main-content {
  /* 固定宽度为1100px */
  width: 1100px;
  /* 左右内边距20px */
  padding: 0 20px;
}

/* 搜索栏卡片样式 */
.search-bar-card {
  /* 白色背景 */
  background: #fff;
  /* 12px圆角 */
  border-radius: 12px;
  /* 内边距：上下15px，左右20px */
  padding: 15px 20px;
  /* 底部外边距20px，与下方元素分隔 */
  margin-bottom: 20px;
  /* 阴影效果：x偏移0，y偏移4px，模糊12px，透明度0.03的黑色 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
}

/* 通知列表卡片 */
.notice-list-card {
  /* 最小高度500px，确保有足够空间显示内容 */
  min-height: 500px;
}

/* 单个通知项样式 */
.notice-item {
  /* 白色背景 */
  background: #fff;
  /* 12px圆角 */
  border-radius: 12px;
  /* 底部外边距15px，项与项之间的间距 */
  margin-bottom: 15px;
  /* 内边距20px */
  padding: 20px;
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上两端对齐 */
  justify-content: space-between;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 鼠标指针变为手形，表示可点击 */
  cursor: pointer;
  /* 轻微阴影效果 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
  /* 透明边框，为hover状态预留 */
  border: 1px solid transparent;
  /* 所有CSS属性变化在0.3秒内完成，使用ease缓动函数 */
  transition: all 0.3s ease;
}

/* 通知项悬停效果 */
.notice-item:hover {
  /* 悬停时阴影加深、范围扩大 */
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  /* 添加绿色边框 */
  border-color: #d1eddb;
  /* 向上移动2px，产生上浮效果 */
  transform: translateY(-2px);
}

/* 通知项左侧内容容器 */
.notice-left {
  /* 使用flex布局 */
  display: flex;
  /* 子元素之间的间距为20px */
  gap: 20px;
  /* 占据剩余空间的比例为1 */
  flex: 1;
}

/* 日期盒子样式 */
.date-box {
  /* 浅灰色背景 */
  background-color: #f8f9fa;
  /* 8px圆角 */
  border-radius: 8px;
  /* 固定宽度60px */
  width: 60px;
  /* 固定高度60px */
  height: 60px;
  /* 使用flex布局 */
  display: flex;
  /* 主轴方向为垂直（列） */
  flex-direction: column;
  /* 主轴（垂直方向）居中对齐 */
  justify-content: center;
  /* 交叉轴（水平方向）居中对齐 */
  align-items: center;
  /* 文字颜色为深灰色 */
  color: #333;
  /* 1px浅灰色实线边框 */
  border: 1px solid #eee;
}

/* 日期中的天数样式 */
.date-box .day {
  /* 字体大小20px */
  font-size: 20px;
  /* 字体加粗 */
  font-weight: 700;
  /* 行高为1，避免额外的行高影响 */
  line-height: 1;
  /* 绿色文字 */
  color: #3AAE6E;
}

/* 日期中的月份样式 */
.date-box .month {
  /* 字体大小12px */
  font-size: 12px;
  /* 浅灰色文字 */
  color: #999;
  /* 顶部外边距2px，与天数分隔 */
  margin-top: 2px;
}

/* 信息区域容器 */
.info-box {
  /* 占据剩余空间的比例为1 */
  flex: 1;
  /* 使用flex布局 */
  display: flex;
  /* 主轴方向为垂直（列） */
  flex-direction: column;
  /* 子元素在主轴（垂直方向）上两端对齐，第一个在顶部，最后一个在底部 */
  justify-content: space-between;
}

/* 标题行容器 */
.title-row {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 子元素之间的间距为10px */
  gap: 10px;
  /* 底部外边距6px，与下方元素分隔 */
  margin-bottom: 6px;
}

/* 标题样式 */
.title {
  /* 移除默认外边距 */
  margin: 0;
  /* 字体大小16px */
  font-size: 16px;
  /* 深灰色文字 */
  color: #333;
  /* 半粗字体 */
  font-weight: 600;
}

/* 摘要样式 */
.summary {
  /* 字体大小13px */
  font-size: 13px;
  /* 中灰色文字 */
  color: #666;
  /* 底部外边距10px，与下方元素分隔 */
  margin-bottom: 10px;
  /* 溢出内容隐藏 */
  overflow: hidden;
  /* 文本溢出时显示省略号 */
  text-overflow: ellipsis;
  /* 强制不换行 */
  white-space: nowrap;
  /* 最大宽度600px，超出部分显示省略号 */
  max-width: 600px;
}

/* 元数据行（时间、查看次数等） */
.meta-row {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在交叉轴上居中对齐 */
  align-items: center;
  /* 子元素之间的间距为20px */
  gap: 20px;
  /* 字体大小12px */
  font-size: 12px;
  /* 浅灰色文字 */
  color: #999;
}

/* 元数据行中的图标样式 */
.meta-row .el-icon {
  /* 垂直方向对齐，向上偏移1px */
  vertical-align: -1px;
  /* 右侧外边距3px，与文字分隔 */
  margin-right: 3px;
}

/* 通知项右侧箭头区域 */
.notice-right {
  /* 浅灰色文字/图标颜色 */
  color: #ccc;
  /* 变换属性在0.3秒内完成 */
  transition: transform 0.3s;
}

/* 悬停时右侧箭头区域样式变化 */
.notice-item:hover .notice-right {
  /* 变为绿色 */
  color: #3AAE6E;
  /* 向右移动5px，产生动态效果 */
  transform: translateX(5px);
}

/* 分页容器 */
.pagination-box {
  /* 使用flex布局 */
  display: flex;
  /* 子元素在主轴上居中对齐 */
  justify-content: center;
  /* 顶部外边距30px，与上方内容分隔 */
  margin-top: 30px;
}

/* 弹窗样式调整 */

/* 弹窗元数据区域（发布时间等） */
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
  /* 底部外边距15px，与内容分隔 */
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