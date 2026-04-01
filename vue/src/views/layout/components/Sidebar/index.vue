<template>
    <el-scrollbar>
        <el-menu
                :default-active="activeMenu"
                class="sidebar-menu"
        >
            <SidebarItem
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
            />
        </el-menu>
    </el-scrollbar>
</template>

<script setup>
import SidebarItem from './SidebarItem.vue'
import useRouteStore from '@/store/modules/routeStore.js'
import {useRoute} from "vue-router";

const route = useRoute()
const routeStore = useRouteStore()

const sidebarRouters = computed(() => routeStore.sidebarRouters)

const activeMenu = computed(() => {
    const {meta, path} = route
    if (meta.activeMenu) {
        return meta.activeMenu
    }
    return path
})
</script>

<style scoped>
.sidebar-menu {
  padding: 4px 0;
}

.sidebar-menu :deep(.el-menu-item) {
  color: #606266;
  height: 30px;
}

.sidebar-menu :deep(.el-menu-item).is-active {
  background: var(--el-color-primary) !important;
  color: #fff !important;
  position: relative;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
}
</style>
