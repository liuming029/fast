<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card>
          <div class="card-content">
            <div class="card-icon" style="background-color:#247bd8">
              <el-icon><Document/></el-icon>
            </div>
            <div>
              <p class="title">总订单数</p>
              <div>
                <span>{{homeCount.orderCount}}</span>
                <span>单</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-content">
            <div class="card-icon" style="background-color:#46cd7d">
              <el-icon><User/></el-icon>
            </div>
            <div>
              <p class="title">用户总数</p>
              <div>
                <span>{{homeCount.userCount}}</span>
                <span>人</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-content">
            <div class="card-icon" style="background-color:#bd9b2b">
              <el-icon><Bicycle/></el-icon>
            </div>
            <div>
              <p class="title">配送人数</p>
              <div>
                <span>{{homeCount.riderCount}}</span>
                <span>人</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-content">
            <div class="card-icon" style="background-color:#a6335b">
              <el-icon><Document/></el-icon>
            </div>
            <div>
              <p class="title">通知公告</p>
              <div>
                <span>{{homeCount.noticeCount}}</span>
                <span>条</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="14">
        <el-card>
          <template #header>
            <span>近14天订单统计趋势</span>
          </template>
          <div ref="orderChartRef" style="height: 550px; width: 100%;"></div>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card>
          <template #header>
            <span>订单状态分布</span>
          </template>
          <!-- 🔧 给饼图容器加内边距，彻底避免边缘截断 -->
          <div ref="statusChartRef" style="height: 550px; width: 100%; padding: 0 20px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Bicycle, Document, User } from "@element-plus/icons-vue";
import { selectHomeCount, selectOrderStatusChart, selectOrderTrend } from "../api/take/homePage.js";
import * as echarts from 'echarts'

const orderChartRef = ref()
const statusChartRef = ref()
let orderChart = null
let statusChart = null

const homeCount = ref({})

// ==================== 饼图（终极修复：100%不遮挡）====================
const initStatusChart = async () => {
  try {
    const res = await selectOrderStatusChart()
    if (!res || !res.data) {
      ElMessage.warning('暂无订单状态数据')
      return
    }

    const statusData = res.data.map(item => ({
      name: item.status,
      value: item.count
    }))

    statusChart = echarts.init(statusChartRef.value)
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}单 ({d}%)'
      },
      // 🔧 图例放在饼图右侧，完全不遮挡扇区
      legend: {
        orient: 'vertical',
        right: 10,
        top: 'middle',
        textStyle: {
          fontSize: 14,
          lineHeight: 22
        },
        itemWidth: 14,
        itemHeight: 14
      },
      // 🔧 饼图左移，给右侧图例留足空间
      series: [
        {
          name: '订单状态',
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['40%', '50%'], // 左移饼图，给右侧图例腾位置
          avoidLabelOverlap: true,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          // 🔧 标签放在饼图内部，彻底解决外部遮挡
          label: {
            show: true,
            position: 'inside',
            fontSize: 13,
            fontWeight: 'bold',
            color: '#fff',
            formatter: '{b}\n{c}单'
          },
          // 🔧 隐藏外部引导线，避免遮挡
          labelLine: {
            show: false
          },
          emphasis: {
            scale: true,
            scaleSize: 10
          },
          data: statusData
        }
      ],
      // 🔧 给图表加内边距，避免边缘截断
      grid: {
        left: '5%',
        right: '25%',
        top: '10%',
        bottom: '10%'
      }
    }
    statusChart.setOption(option)

    window.addEventListener('resize', () => statusChart.resize())
  } catch (err) {
    console.error('状态图表加载失败:', err)
    ElMessage.error('状态图表加载失败')
  }
}

// ==================== 订单趋势折线图（优化版）====================
const initOrderChart = async () => {
  try {
    const res = await selectOrderTrend()
    if (!res || !res.data) return

    orderChart = echarts.init(orderChartRef.value)
    const option = {
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: res.data.dates,
        axisLabel: {
          rotate: 45,
          fontSize: 12
        }
      },
      yAxis: {
        type: 'value',
        min: 0,
        axisLabel: {
          fontSize: 12
        }
      },
      series: [{
        data: res.data.counts,
        type: 'line',
        smooth: true,
        itemStyle: { color: '#409EFF' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0)' }
          ])
        }
      }],
      grid: {
        left: '10%',
        right: '5%',
        bottom: '15%',
        top: '10%'
      }
    }
    orderChart.setOption(option)
    window.addEventListener('resize', () => orderChart.resize())
  } catch (err) {
    console.error('趋势图表加载失败:', err)
  }
}

// ==================== 页面加载 ====================
onMounted(async () => {
  const homeRes = await selectHomeCount()
  homeCount.value = homeRes.data

  initOrderChart()
  initStatusChart()
})
</script>

<style scoped>
.card-content{
  display: flex;
  align-items: center;
  padding: 5px 0;
}
.card-icon{
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color:#fff;
  font-size: 30px;
}
.title{
  font-weight: bold;
  margin: 0 0 8px 0;
}
</style>