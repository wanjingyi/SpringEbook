<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

            <a-button type="primary" @click="handleAdd">新增</a-button>

            <a-table :columns="columns" :data-source="ebooks" :pagination="pagination" :loading="loading"
                @change="handleTableChange">
                <template #headerCell="{ column }">
                    <!-- <template>
                        <img  v-if="column.key === 'cover'" src="cover" alt="">
                    </template> -->
                    <template v-if="column.key === 'name'">
                        <span>
                            <smile-outlined />
                            Name
                        </span>
                    </template>
                </template>

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

        <a-modal v-model:open="open" title="电子书表单" @ok="handleOk">
            <a-form :model="modalData" name="nest-messages">
                <a-form-item label="封面">
                    <a-input v-model:value="modalData.cover" />
                </a-form-item>
                <a-form-item label="名称">
                    <a-input v-model:value="modalData.name" />
                </a-form-item>
                <a-form-item label="分类一">
                    <a-input v-model:value="modalData.category1_id" />
                </a-form-item>
                <a-form-item label="分类二">
                    <a-input v-model:value="modalData.category2_id" />
                </a-form-item>
                <a-form-item label="描述">
                    <a-input v-model:value="modalData.description" type="textarea" />
                </a-form-item>

            </a-form>
        </a-modal>
    </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';

export default defineComponent({
    name: 'adminEbook',
    setup() {

        const pagination = computed(() => ({
            current: 1,
            pageSize: 5,
            total: 8

        }));

        const loading = ref(false);

        const columns = [
            {
                name: '封面',
                dataIndex: 'cover',
                key: 'cover',
                with: 200
            },
            {
                name: '名称',
                dataIndex: 'name',
                key: 'name',
                with: 200
            },
            {
                title: '分类1',
                dataIndex: 'category1_id',
                key: 'category1_id',
                with: 100
            },
            {
                title: '分类2',
                dataIndex: 'category2_id',
                key: 'category2_id',
                with: 100
            },
            {
                title: '描述',
                key: 'description',
                dataIndex: 'description',
                with: 200
            },
            {
                title: '封面',
                key: 'cover',
                with: 100
            },
            {
                title: '文档数',
                key: 'doc_count',
                with: 100
            },
            {
                title: '阅读数',
                key: 'view_count',
                with: 100
            },
            {
                title: '点赞数',
                key: 'vote_count',
                with: 100
            },
            {
                title: 'Action',
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
            axios.post("/ebook/save", modalData.value).then((response) => {
                // console.log(response,'222222');
                const data = response.data;
                if (data.success) {
                    open.value = false;

                    //重新加载
                    handleQuery({
                        page: pagination.value.current,
                        size: pagination.value.pageSize
                    });
                }else {
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
            axios.delete("/ebook/delete/" + id).then((response) => {
                console.log(response, '2222');
                const data = response.data;
                if (data.success) {
                    //重新加载
                    handleQuery({
                        page: pagination.value.current,
                        size: pagination.value.pageSize
                    });
                }
            });
        };

        const ebooks = ref();

        /**
        * 数据查询
        */
        const handleQuery = (params: any) => {
            loading.value = true;
            axios.get("/ebook/list", {
                params: {
                    page: params.page,
                    size: params.size,
                }
            }).then((response) => {
                loading.value = false;
                const data = response.data;

                if (data.success) {
                    ebooks.value = data.content.list;

                    // 重置分页按钮
                    pagination.value.current = params.current;
                    pagination.value.total = data.content.total;
                }else {
                    message.error(data.message);
                }

            });
        };

        const handleTableChange = (pagination: any) => {
            console.log("看看你自带的分页参数都有啥", pagination)
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize
            });
        };



        onMounted(() => {
            handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
            });
        })

        return {
            ebooks,
            columns,
            loading,
            pagination,
            handleTableChange,
            handleEdit,
            handleOk,
            open,
            modalData,
            handleAdd,
            handleDelete
        }
    }
});
</script>