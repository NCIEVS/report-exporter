import { createWebHistory, createRouter } from "vue-router";

import About from "@/components/About.vue";
import Exports from "@/components/Exports.vue";
import Documentation from "@/components/Documentation.vue";
import ReportSelection from "@/components/ReportSelection.vue";
import ReadCodeEntry from "@/components/ReadCodeEntry.vue";
import SearchTermEntry from "@/components/SearchTermEntry.vue";
import ResolveBranchEntry from "@/components/ResolveBranchEntry.vue";
import Roles from "@/components/Roles.vue";
import Associations from "@/components/Associations.vue";

const routes = [
    { path: "/",
        name: "Report Selection",
        component: ReportSelection,
    },

    { path: "/ReportSelection",
        name: "Report Selection",
        component: ReportSelection,
    },

    { path: '/readCodeEntry', component: ReadCodeEntry, props: { msg: "this is it", selectedTags:[] }},
    { path: '/searchTermEntry', component: SearchTermEntry },
    { path: '/resolveBranchEntry', component: ResolveBranchEntry },
    { path: '/roles', component: Roles },
    { path: '/associations', component: Associations },
    { path: '/about', component: About },
    { path: '/documentation', component: Documentation },
    { path: "/exports", component: Exports },
    // if page is unknown, show main selection page.

    {
        path: '/:pathMatch(.*)*',
        redirect: "/ReportSelection",
    },
    ];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;