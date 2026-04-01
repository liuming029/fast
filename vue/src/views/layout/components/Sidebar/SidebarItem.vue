<template>
  <div v-if="!item.hidden">
    <template v-if="shouldShowSingleItem">
      <app-link v-if="onlyOneChild.meta" :to="singleItemPath">
        <el-menu-item :index="singleItemPath">
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)" style="margin-right: 8px;" />
          <template #title>
            <span :title="getTitle(onlyOneChild.meta.title)" style="margin-left: 2px;">{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else :index="resolvePath(item.path)" teleported>
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta.icon" style="margin-right: 10px;" />
        <span :title="getTitle(item.meta.title)" style="margin-left: 2px;">{{ item.meta.title }}</span>
      </template>

      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(child.path)"
        is-nest
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import AppLink from './Link'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const onlyOneChild = computed(() => {
  const children = props.item.children || []
  const showingChildren = children.filter(item => !item.hidden)

  if (showingChildren.length === 1) {
    return showingChildren[0]
  }

  if (showingChildren.length === 0) {
    return { ...props.item, path: '', noShowingChildren: true }
  }

  return null
})

const shouldShowSingleItem = computed(() => {
  return onlyOneChild.value &&
         (!onlyOneChild.value.children || onlyOneChild.value.noShowingChildren) &&
         !props.item.alwaysShow
})

const singleItemPath = computed(() => {
  return resolvePath(onlyOneChild.value.path)
})

const resolvePath = (routePath) => {
  const fullPath = props.basePath + '/' + routePath
  if (fullPath.length === 0 || !fullPath) {
    return fullPath
  }
  let res = fullPath.replace('//', '/')
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1)
  }
  return res
}

const getTitle = (title) => title?.length > 5 ? title : ""
</script>

<style scoped>
/* 🔥 🔥 🔥 强制覆盖所有样式，100% 生效 */
:deep(.el-menu-item) {
  height: 50px !important;
  line-height: 50px !important;
  padding: 0 12px !important;
  margin: 5px 0 !important;
  border-radius: 4px;
}

:deep(.el-sub-menu__title) {
  height: 56px !important;
  line-height: 56px !important;
  padding: 0 12px !important;
  margin: 6px 0 !important;
  border-radius: 4px;
}

/* 子菜单也拉开 */
:deep(.el-menu .el-sub-menu) {
  margin-bottom: 12px !important;
}
</style>
