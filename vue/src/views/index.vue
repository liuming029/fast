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
  </div>
</template>

<script setup>

import {Bicycle, Document} from "@element-plus/icons-vue";
import {selectHomeCount} from "../api/take/homePage.js";


//统计数据
const homeCount = ref({})

onMounted(()=>{
  selectHomeCount().then(res =>{
    homeCount.value = res.data
  })
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
