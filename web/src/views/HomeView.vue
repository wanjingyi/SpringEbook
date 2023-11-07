<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu mode="inline" @click="handleClick" :openKeys="openKeys">
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span>
              <user-outlined />
              {{ item.name }}
            </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">{{ child.name }}</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <div class="welcome" v-show="isShowWelcome">
        <the-welcome></the-welcome>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :pagination="pagination"
        :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { Tool } from '@/util/tool';
import { message } from 'ant-design-vue';
import TheWelcome from '@/components/the-welcome.vue';


export default defineComponent({
  name: 'HomeView',
  components: {
    TheWelcome
  },
  setup() {
    const openKeys = ref();

    const isShowWelcome = ref(true);

    let categoryId2 = 0;

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };

    const ebooks = ref();

    /**
     * 查询电子书
     */
    const handleQueryEbook = () => {
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 100,
          categoryId2: categoryId2
        }
      }).then((response) => {
        console.log(response,'111111');
        const data = response.data;
        ebooks.value = data.content.list
      })
    };

    /**
    * 数据查询
    */
    const level1 = ref();//一级分类数组
    const category = ref();
    const handleQuery = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;

        if (data.success) {
          category.value = data.content;

          level1.value = [];

          level1.value = Tool.array2Tree(category.value, 0);

        } else {
          message.error(data.message);
        }

      });
    };

    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    };

    onMounted(() => {
      handleQuery();
    });


    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    return {
      ebooks,
      actions,
      level1,
      openKeys,
      handleClick,
      isShowWelcome,
      pagination
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
