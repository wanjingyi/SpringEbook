<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

            <a-button type="primary" @click="handleAdd">新增</a-button>

            <a-table :columns="columns" :data-source="level1" :loading="loading">
                <!-- <template #headerCell="{ column }">
                    <template v-if="column.key === 'name'">
                        <span>
                            <smile-outlined />
                            Name
                        </span>
                    </template>
                </template> -->

                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'name'">
                        <a>
                            {{ record.name }}
                        </a>
                    </template>
                    <template v-else-if="column.key === 'tags'">
                        <span>
                            <a-tag v-for="tag in record.tags" :key="tag"
                                :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'">
                                {{ tag.toUpperCase() }}
                            </a-tag>
                        </span>
                    </template>
                    <template v-else-if="column.key === 'action'">
                        <a-space wrap>
                            <a-button type="primary" @click="handleEdit(record)">编辑</a-button>

                            <a-popconfirm title="删除后不可以恢复，确认删除?" ok-text="是" cancel-text="否"
                                @confirm="handleDelete(record.id)">
                                <a-button type="primary" danger>删除</a-button>
                            </a-popconfirm>
                        </a-space>
                    </template>
                </template>
            </a-table>
        </a-layout-content>

        <a-modal v-model:open="open" title="分类表单" @ok="handleOk">
            <a-form :model="modalData" name="nest-messages">
                <a-form-item label="名称">
                    <a-input v-model:value="modalData.name" />
                </a-form-item>
                <a-form-item label="父分类">
                    <!-- <a-input v-model:value="modalData.parent" /> -->

                    <a-select ref="select" v-model:value="modalData.parent">
                        <a-select-option value="0">无</a-select-option>
                        <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="modalData.id === c.id">
                            {{c.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="顺序">
                    <a-input v-model:value="modalData.sort" />
                </a-form-item>
            </a-form>
        </a-modal>
    </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool';

export default defineComponent({
    name: 'adminCategory',
    setup() {

        const loading = ref(false);

        const columns = [
            {
                title: '名称',
                name: '名称',
                dataIndex: 'name',
                key: 'name',
                with: 200
            },
            {
                title: '父分类',
                name: '父分类',
                dataIndex: 'parent',
                key: 'parent',
                with: 200
            },
            {
                title: '顺序',
                dataIndex: 'sort',
                key: 'sort',
                with: 100
            },
            {
                title: '操作',
                key: 'action',
                // slots: {customRender: 'action'}
            }
        ];
        /**
        * 模态框数据
        */
        const modalData = ref();
        const open = ref<boolean>(false);

        const handleEdit = (record: any) => {
            open.value = true;
            modalData.value = record;
        };

        const handleOk = (e: MouseEvent) => {
            open.value = true;
            axios.post("/category/save", modalData.value).then((response) => {

                const data = response.data;
                if (data.success) {
                    open.value = false;

                    //重新加载
                    handleQuery();
                } else {
                    message.error(data.message);
                }
            });
        };

        /**
        * 新增
        */
        const handleAdd = () => {
            open.value = true;
            modalData.value = {};
        };

        /**
         * 删除
         */
        const handleDelete = (id: number) => {
            axios.delete("/category/delete/" + id).then((response) => {
                const data = response.data;
                if (data.success) {
                    //重新加载
                    handleQuery();
                }
            });
        };

        const category = ref();

        /**
        * 数据查询
        */
        const level1 = ref();//一级分类数组
        const handleQuery = () => {
            loading.value = true;
            axios.get("/category/list").then((response) => {
                loading.value = false;
                const data = response.data;

                if (data.success) {
                    category.value = data.content.list;
                    console.log(category, '1111');

                    level1.value = [];

                    level1.value = Tool.array2Tree(category.value, 0);

                } else {
                    message.error(data.message);
                }

            });
        };



        onMounted(() => {
            handleQuery();
        })

        return {
            category,
            columns,
            loading,
            handleEdit,
            handleOk,
            open,
            modalData,
            handleAdd,
            handleDelete,
            level1
        }
    }
});
</script>