<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <a-table :columns="columns" :data-source="ebooks" :pagination="pagination" :loading="loading"
                @change="handleTableChange">
                <template #headerCell="{ column }">
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
                        <a-popconfirm  title="Sure to delete?">
                            <a>Delete</a>
                        </a-popconfirm>
                    </template>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';

export default defineComponent({
    name: 'adminEbook',
    setup() {

        const pagination = computed(() => ({
            total: 0,
            current: 1,
            pageSize: 5
        }));

        const loading = ref(false);

        const columns = [
            {
                name: 'name',
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

        const ebooks = ref();

        /**
        * 数据查询
        */
        const handleQuery = (params: any) => {
            // loading.value = true;
            axios.get("/ebook/list", params).then((response) => {
                const data = response.data;
                ebooks.value = data.content;
                
                pagination.value.current = params.pageSize;
            });
        };

        const handleTableChange = (pagination: any) => {
            console.log(pagination)
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize
            });
        };

        onMounted(() => {
            handleQuery({});
        })

        return {
            ebooks,
            columns,
            loading,
            pagination,
            handleTableChange
        }
    }
});
</script>